package com.social.app.repository;

import com.social.app.model.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends MongoRepository<PostModel, String> {
    Optional<PostModel> findByIdAndUserId(String postId, String userId);

    List<PostModel> findByUserId(String userId);
    @Query("{ 'userId' : ?0,'isPublished': true }")
    List<PostModel> findByUserIdAndIsPublished(String userId);
    @Query("{'isPublished':true}")
    List<PostModel> findAllPublishedPost();

    @Query("{'_id' : ?0,'isPublished':true}")
    Optional<PostModel> findByIdAndIsPublished(String postId);
}
