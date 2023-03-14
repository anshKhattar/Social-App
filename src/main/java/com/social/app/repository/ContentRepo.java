package com.social.app.repository;

import com.social.app.model.ContentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepo extends MongoRepository<ContentModel,String> {
}
