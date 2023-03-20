package com.social.app.repository;

import com.social.app.model.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepo extends MongoRepository<PostModel, String> {
    Optional<PostModel> findByIdAndUserId(String postId, String userId);
}
