package com.social.app.service;

import com.social.app.helpers.CloudinaryService;
import com.social.app.model.ContentModel;
import com.social.app.repository.ContentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepo contentRepo;
    private final CloudinaryService cloudinaryService;
    public ContentModel createContent(ContentModel newContent){
        return contentRepo.save(newContent);
    }

    public String uploadContent(MultipartFile content) {
        return cloudinaryService.upload(content);
    }
    public ContentModel getContentById(String id){
        return contentRepo.findById(id).get();
    }
}
