package com.social.app.repository;

import com.social.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
