package com.social.app.repository;

import com.social.app.model.CommentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends MongoRepository<CommentModel, String> {
    Optional<CommentModel> findByIdAndUserId(String commentId, String userId);
    List<CommentModel> findAllByPostId(String postId);
    void deleteAllByPostId(String postId);
    void deleteAllByParentId(String parentId);
}
