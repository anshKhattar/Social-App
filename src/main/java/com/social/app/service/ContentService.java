package com.social.app.service;

import com.social.app.enums.ContentTypeEnum;
import com.social.app.helpers.CloudinaryService;
import com.social.app.model.ContentModel;
import com.social.app.repository.ContentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void deleteById(String id){
        cloudinaryService.deleteContentByUrl(
                getContentById(id).getLink()
        );
        contentRepo.deleteById(id);
    }
    public ContentTypeEnum getContentType(MultipartFile file){
        if(file == null || file.getContentType() == null) return null;
        String contentType = file.getContentType().split("/")[0];
        if(contentType.equals("image")) return ContentTypeEnum.IMAGE;
        if(contentType.equals("video")) return ContentTypeEnum.VIDEO;
        return null;
    }
}
