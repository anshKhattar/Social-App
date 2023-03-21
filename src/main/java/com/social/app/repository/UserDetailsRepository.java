package com.social.app.repository;

import com.social.app.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDetailsRepository extends MongoRepository<UserDetails,String> {

    Optional<UserDetails> findByUserId(String userId);
}
