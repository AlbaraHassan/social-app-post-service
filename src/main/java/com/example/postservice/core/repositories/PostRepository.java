package com.example.postservice.core.repositories;


import com.example.postservice.rest.models.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends MongoRepository<PostModel, String> {
  void deleteAllByCreatedBy(String id);
}
