package com.example.postservice.rest.dtos;


import com.example.postservice.rest.models.PostModel;

public class PostCreateDTO {
  private String content;

  private String createdBy;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public PostModel toEntity(){
    PostModel post = new PostModel();
    post.setContent(this.content);
    post.setCreatedBy(this.createdBy);
    return post;
  }
}
