package com.social.app.repository;

import com.social.app.model.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepo extends MongoRepository<PostModel, String> {
}
