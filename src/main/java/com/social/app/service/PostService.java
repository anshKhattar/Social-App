package com.social.app.service;

import com.social.app.dto.PostCreateDTO;
import com.social.app.dto.PostResponseDTO;
import com.social.app.enums.VoteTypeEnum;
import com.social.app.model.ContentModel;
import com.social.app.model.PostModel;
import com.social.app.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final  VoteService voteService;
    private final ContentService contentService;

    public List<PostModel> getAllPosts() {
        return postRepo.findAll();
    }

    public PostModel createPost(PostCreateDTO newPostDTO){

        PostModel newPost = PostModel.builder()
                .title(newPostDTO.getTitle())
                .description(newPostDTO.getDescription())
                .build();

        if(newPostDTO.getContent() != null){
            ContentModel newContent = ContentModel.builder()
                    .type(newPostDTO.getContentType())
                    .link(contentService.uploadContent(newPostDTO.getContent()))
                    .build();
            ContentModel dbContent = contentService.createContent(newContent);
            newPost.setContentId(dbContent.getId());
        }

        return postRepo.save(newPost);
    }

    public PostModel getPostById(String postId) {
        return postRepo.findById(postId).get();
    }

    public PostResponseDTO postModelToResponse(PostModel postModel){

        PostResponseDTO resPost = PostResponseDTO.builder()
                .id(postModel.getId())
                .title(postModel.getTitle())
                .description(postModel.getDescription()).build();

        if(postModel.getContentId() != null){
            ContentModel dbContent = contentService.getContentById(postModel.getContentId());
            resPost.setContentLink(dbContent.getLink());
            resPost.setContentType(dbContent.getType());
        }
        return resPost;
    }
    public void votePost(String postId, VoteTypeEnum voteType){
        //if vote does not exist for post&user create
        //else if exist and type is same then delete
        //or else exist and type is different update


//        if(voteService.getVotesByPostId(id) == null)
//            return voteService.createVote()
    }

    public void deletePostById(String postId) {
        postRepo.deleteById(postId);
    }
}
