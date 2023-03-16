package com.social.app.repository;

import com.social.app.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<UserDetails,String> {

}
