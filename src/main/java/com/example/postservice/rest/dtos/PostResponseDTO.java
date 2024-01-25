package com.example.postservice.rest.dtos;

import com.example.postservice.rest.models.PostModel;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

public class PostResponseDTO {
  @NotBlank
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @NotBlank
  private String content;
  @NotBlank
  private UserDTO createdBy;

  @NotBlank
  private List<UserDTO> likes = new LinkedList<>();

  public List<UserDTO> getLikes() {
    return likes;
  }

  public void setLikes(List<UserDTO> likes) {
    this.likes = likes;
  }

  public PostResponseDTO(PostModel post){
    this.content = post.getContent();
    this.id = post.getId();
  }

  public PostResponseDTO(PostModel post, UserDTO user){
    this.content = post.getContent();
    this.id= post.getId();
    this.createdBy = user;
  }

  public UserDTO getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserDTO createdBy) {
    this.createdBy = createdBy;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
