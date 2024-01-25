package com.example.postservice.rest.services;


import com.example.postservice.core.context.UserContext;
import com.example.postservice.core.exceptions.auth.ForbiddenException;
import com.example.postservice.core.exceptions.general.NotFoundException;
import com.example.postservice.core.repositories.PostRepository;
import com.example.postservice.rest.dtos.PostCreateDTO;
import com.example.postservice.rest.dtos.PostResponseDTO;
import com.example.postservice.rest.dtos.PostUpdateDTO;
import com.example.postservice.rest.dtos.UserDTO;
import com.example.postservice.rest.enums.Role;
import com.example.postservice.rest.feign.UserService;
import com.example.postservice.rest.models.PostModel;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;
  private final UserContext userContext;

  public PostService(PostRepository postRepository,
                     UserService userService,
                     UserContext userContext) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.userContext = userContext;
  }


  public PageImpl<PostResponseDTO> getAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

    List<PostResponseDTO> postResponseDTOs = this.postRepository.findAll(pageable)
      .stream()
      .map(post -> {
        UserDTO userDTO = this.userService.get(post.getCreatedBy())
          .orElse(null);

        PostResponseDTO postResponseDTO = new PostResponseDTO(post, userDTO);
        postResponseDTO.setLikes(this.getLikes(post.getId()));

        return postResponseDTO;
      })
      .collect(Collectors.toList());

    long total = this.postRepository.count();

    return new PageImpl<>(postResponseDTOs, pageable, total);
  }


  public PostResponseDTO get(String id) {
    return this.postRepository.findById(id).map(post -> {

      UserDTO userDTO = this.userService.get(post.getCreatedBy())
        .orElse(null);
    PostResponseDTO postResponseDTO = new PostResponseDTO(post, userDTO);
    postResponseDTO.setLikes(this.getLikes(id));
      return postResponseDTO;
    }).orElseThrow(()-> new NotFoundException("Post is not found"));
  }

  public PostResponseDTO create(PostCreateDTO data) {
    data.setCreatedBy(this.userContext.getId());
    data.setContent(data.getContent().trim());
    UserDTO user = this.userService.get(data.getCreatedBy())
      .orElse(null);
    return new PostResponseDTO(this.postRepository.save(data.toEntity()), user);
  }

  public PostResponseDTO update(String id, PostUpdateDTO updatedPost) {
    Optional<PostModel> post = this.postRepository.findById(id);
    updatedPost.setContent(updatedPost.getContent().trim());
    post.ifPresent(postModel -> {
      if (!this.userContext.getId().equals(postModel.getCreatedBy())) {
        throw new ForbiddenException("This post can only be updated by its original creator");
      }
      PostModel foundPost = post.get();
      foundPost.setContent(updatedPost.getContent());
      this.postRepository.save(foundPost);
    });
    return post.map(postModel -> {
      UserDTO user = this.userService.get(postModel.getCreatedBy()).orElse(null);
      PostResponseDTO postResponseDTO = new PostResponseDTO(postModel, user);
      postResponseDTO.setLikes(this.getLikes(id));
      return postResponseDTO;
    }).orElseThrow(() -> new NotFoundException("Post does not exist"));


  }

  public int like(String id) {
    Optional<PostModel> post = this.postRepository.findById(id);
    post.ifPresent(postModel -> {
      postModel.setLikes(this.userContext.getId());
      this.postRepository.save(postModel);
    });
    return post.orElseThrow(() -> new NotFoundException("Post does not exist")).getLikes().size();
  }

  public List<UserDTO> getLikes(String id) {
    PostModel postModel = this.postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post does not exist"));
    PostResponseDTO post = new PostResponseDTO(postModel);
    List<UserDTO> users =  postModel.getLikes().stream().map(userId -> this.userService.get(String.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found"))).toList();
    post.setLikes(users);
    return post.getLikes();
  }

  public boolean delete(String id) {
    Optional<PostModel> post = this.postRepository.findById(id);
    if (post.isPresent()) {
      PostModel postModel = post.get();
      if (!this.userContext.getId().equals(postModel.getCreatedBy()) && !this.userContext.getRole().equals(Role.ADMIN)) {
        throw new ForbiddenException("This post can only be deleted by its original creator or an admin");
      }
      this.postRepository.delete(postModel);
    } else {
      throw new NotFoundException("Post does not exist");
    }
    return true;
  }
}
