package com.example.postservice.rest.dtos;


import com.example.postservice.rest.models.PostModel;

public class PostUpdateDTO {
  private String content;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public PostModel toEntity(){
    PostModel post = new PostModel();
    post.setContent(this.content);
    return post;
  }

}
