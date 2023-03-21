package com.social.app.repository;

import com.social.app.model.CommentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<CommentModel, String> {
List<CommentModel> findAllByPostId(String postId);
void deleteAllByPostId(String postId);
}
