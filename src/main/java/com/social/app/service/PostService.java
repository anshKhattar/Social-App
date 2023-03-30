package com.social.app.service;

import com.social.app.dto.PostCreateDTO;
import com.social.app.dto.PostResponseDTO;
import com.social.app.enums.VoteTypeEnum;
import com.social.app.model.CommentModel;
import com.social.app.model.ContentModel;
import com.social.app.model.PostModel;
import com.social.app.model.VoteModel;
import com.social.app.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final  VoteService voteService;
    private final ContentService contentService;
    private final CommentService commentService;
    private final UserDetailsServiceImpl userDetailsService;

    public List<PostModel> getAllPosts() {
        return postRepo.findAllPublishedPost();
    }

    @Transactional
    public PostModel createPost(PostCreateDTO newPostDTO,String userId){

        PostModel newPost = PostModel.builder()
                .description(newPostDTO.getDescription())
                .userId(userId)
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

    public PostModel getPostById(String postId,String loggedInUserId) {
       PostModel dbPost = postRepo.findByIdAndUserId(postId, loggedInUserId).orElse(null);
        if(dbPost != null)
            return dbPost;
        else
            return postRepo.findByIdAndIsPublished(postId).orElse(null);
    }

    public PostResponseDTO postModelToResponse(PostModel postModel){
        if(postModel == null) return null;

        PostResponseDTO resPost = PostResponseDTO.builder()
                .id(postModel.getId())
                .user(userDetailsService.loadUserByUserId(postModel.getUserId()))
                .description(postModel.getDescription())
                .votes(voteService.getVoteCountByPost(postModel.getId()))
                .isPublished(postModel.isPublished())
                .build();

        if(postModel.getContentId() != null){
            ContentModel dbContent = contentService.getContentById(postModel.getContentId());
            resPost.setContentLink(dbContent.getLink());
            resPost.setContentType(dbContent.getType());
        }
        return resPost;
    }
    public String votePost(String postId,String userId, VoteTypeEnum voteType){
        VoteModel dbVote =voteService.getVoteByPostIdAndUserId(postId, userId);
        if(dbVote == null) {
            VoteModel newVote = VoteModel.builder()
                    .voteType(voteType)
                    .userId(userId)
                    .postId(postId)
                    .build();
             voteService.createVote(newVote);
             return "Vote Created";
        }
        if(dbVote.getVoteType() == voteType){
            voteService.deleteVote(dbVote.getId());
            return "Vote Removed";
        }else{
            voteService.updateVote(dbVote.getId(), voteType);
            return "Vote Updated";
        }

    }
@Transactional
    public void deletePostById(String postId,String userId) {
        PostModel dbPost = postRepo.findByIdAndUserId(postId, userId).get();

        commentService.deleteAllByPostId(dbPost.getId());
        voteService.deleteAllByPostId(dbPost.getId());
        if(dbPost.getContentId() != null)
            contentService.deleteById(dbPost.getContentId());

        postRepo.deleteById(dbPost.getId());
    }
    public String changePublishState(String postId,String userId, boolean isPublished) {
        PostModel dbPost = postRepo.findByIdAndUserId(postId, userId).get();
        dbPost.setPublished(isPublished);
        postRepo.save(dbPost);
        return "Post updated";
    }

    public List<PostModel> getPostsByUserId(String userId,String loggedInUserId) {
        if(userId.equals(loggedInUserId))
            return postRepo.findByUserId(userId);
        else
            return postRepo.findByUserIdAndIsPublished(userId);
    }

    public PostModel updatePost(PostCreateDTO updatePost, String userId) {

        PostModel dbPost  = postRepo.findByIdAndUserId(updatePost.getId(),userId).get();

        if(updatePost.getDescription() != null)
            dbPost.setDescription(updatePost.getDescription());

        if(updatePost.getContent() != null && updatePost.getContentType() != null
        ){
            if(dbPost.getContentId() != null)
                 contentService.deleteById(dbPost.getContentId());

            ContentModel newContent = ContentModel.builder()
                    .type(updatePost.getContentType())
                    .link(contentService.uploadContent(updatePost.getContent()))
                    .build();
            ContentModel dbContent = contentService.createContent(newContent);
            dbPost.setContentId(dbContent.getId());

        }else if(updatePost.getContent() == null
                && updatePost.getContentType() == null
                && dbPost.getContentId() == null
        ){
            // System.out.println(updatePost.getContent() + " "+updatePost.getContentType());

            contentService.deleteById(dbPost.getContentId());
            dbPost.setContentId(null);
        }
        return postRepo.save(dbPost);
    }
    public CommentModel createComment(CommentModel newComment, String userId){
        PostModel dbPost = getPostById(newComment.getPostId(),userId);
        if(dbPost == null) return null ;

        if(newComment.getParentId() != null)
            commentService.getCommentById(newComment.getParentId()).get();

        newComment.setUserId(userId);
        return commentService.createComment(newComment);
    }

    public List<CommentModel> getAllCommentsByPostId(String postId, String userId){
        PostModel dbPost = getPostById(postId,userId);
        if(dbPost == null) return null ;
        return commentService.findAllByPostId(postId);
    }
}
