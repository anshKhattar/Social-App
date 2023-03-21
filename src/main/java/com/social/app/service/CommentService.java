package com.social.app.service;

import com.social.app.model.CommentModel;
import com.social.app.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;

    //create comment
    //create reply

//    public CommentModel createComment(String description, String postId){
//        CommentModel newComment = CommentModel.builder()
//                .description(description)
//                .post_id(postId)
//                .user_id()
//                .build()
//        return commentRepo.save();
//    }

    public List<CommentModel> getAllCommentsByPostId(String postId){
        return commentRepo.findAllByPostId(postId);
    }
    public void deleteAllByPostId(String postId){
        commentRepo.deleteAllByPostId(postId);
    }


}
