package com.social.app.repository;

import com.social.app.model.VoteModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepo extends MongoRepository <VoteModel, String> {
}
