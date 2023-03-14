package com.social.app.repository;

import com.social.app.model.CommentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepo extends MongoRepository<CommentModel, String> {
}
