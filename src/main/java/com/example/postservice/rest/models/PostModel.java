package com.example.postservice.rest.models;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Document("post")
public class PostModel {

  @Id
  private String id;

  private String content;

  @CreatedBy
  private String createdBy;

  @CreatedDate
  private Date createdAt = new Date();

  private Set<String> likes = new HashSet<>();

  public Set<String> getLikes() {
    return likes;
  }

  public void setLikes(String like) {
    if (this.likes.contains(like)) {
      this.likes.remove(like);
    } else {
      this.likes.add(like);
    }
  }

  public PostModel() {
  } // Needed when creating a new post

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt){
    this.createdAt = createdAt;
  }

  public PostModel(String content) {
    this.content = content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getId() {
    return this.id;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public String getContent() {
    return this.content;
  }
}