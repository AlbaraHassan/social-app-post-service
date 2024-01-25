package com.example.postservice.rest.controllers;


import com.example.postservice.core.auth.Auth;
import com.example.postservice.rest.dtos.PostCreateDTO;
import com.example.postservice.rest.dtos.PostResponseDTO;
import com.example.postservice.rest.dtos.PostUpdateDTO;
import com.example.postservice.rest.dtos.UserDTO;
import com.example.postservice.rest.services.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/post")
@Tag(name = "Post")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @Auth
  @GetMapping("/all")
  public PageImpl<PostResponseDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
    return this.postService.getAll(page, size);
  }

  @Auth
  @GetMapping
  public PostResponseDTO get(@RequestParam String id) {
    return this.postService.get(id);
  }

  @Auth
  @PostMapping
  public PostResponseDTO create(@RequestBody PostCreateDTO body) {
    return this.postService.create(body);
  }


  @Auth
  @PatchMapping
  public PostResponseDTO update(@RequestParam String id, @RequestBody PostUpdateDTO body) {
    return this.postService.update(id, body);
  }

  @Auth
  @PatchMapping("/like")
  public int like(@RequestParam String id) {
    return this.postService.like(id);
  }

  @Auth
  @GetMapping("/likes")
  public List<UserDTO> getLikes(@RequestParam String id) {
    return this.postService.getLikes(id);
  }

  @Auth
  @DeleteMapping
  public boolean delete(@RequestParam String id) {
    return this.postService.delete(id);
  }
}
