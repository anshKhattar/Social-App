package com.social.app.repository;

import com.social.app.model.VoteModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepo extends MongoRepository <VoteModel, String> {
    List<VoteModel> findAllByPostId(String postId);
    Optional<VoteModel> findAllByPostIdAndUserId(String postId, String userId);
}
