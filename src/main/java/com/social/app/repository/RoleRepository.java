package com.social.app.repository;


import com.social.app.enums.RoleTypeEnum;
import com.social.app.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleTypeEnum name);
}
