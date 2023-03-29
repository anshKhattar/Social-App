package com.social.app.service;

import com.social.app.model.CommentModel;
import com.social.app.model.PostModel;
import com.social.app.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;

    public CommentModel createComment(CommentModel newComment){
        return commentRepo.save(newComment);
    }
    public void deleteAllByPostId(String postId){
        commentRepo.deleteAllByPostId(postId);
    }
    private void deleteCommentById(String commentId){
        commentRepo.deleteById(commentId);
    }
    private void deleteCommentsByParentId(String parentId){
        commentRepo.deleteAllByParentId(parentId);
    }
    public void deleteCommentById(String commentId, String userId){
        CommentModel dbComment = commentRepo.findByIdAndUserId(commentId,userId).get();
        deleteCommentsByParentId(dbComment.getId());
        deleteCommentById(dbComment.getId());
    }


    public Optional<CommentModel> getCommentById(String parentId) {
        return commentRepo.findById(parentId);
    }

    public List<CommentModel> findAllByPostId(String postId) {
        return commentRepo.findAllByPostId(postId);
    }
}
