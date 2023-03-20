package com.social.app.controller;

import com.social.app.dto.PostCreateDTO;
import com.social.app.dto.PostResponseDTO;
import com.social.app.enums.VoteTypeEnum;
import com.social.app.model.PostModel;
import com.social.app.model.User;
import com.social.app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/")
    public PostResponseDTO createPost (@ModelAttribute PostCreateDTO newPost, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        PostModel dbPost =postService.createPost(newPost,user.getId());
        return postService.postModelToResponse(dbPost);
    }

    @GetMapping("/")
    public List<PostResponseDTO>  getAllPosts(){
        List<PostModel> dbPosts = postService.getAllPosts();
        return dbPosts.stream().map(
                dbPost->postService.postModelToResponse(dbPost)
        ).collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public PostResponseDTO getPostById(@PathVariable String postId){
        PostModel dbPost =  postService.getPostById(postId);
        return postService.postModelToResponse(dbPost);
    }

    @DeleteMapping("/{postId}")
    public String deletePostById(@PathVariable String postId, Authentication authentication){
        User user = (User) authentication.getPrincipal();
         postService.deletePostById(postId,user.getId());
         return "Post Deleted";
    }

    @PostMapping("/publish/{postId}")
    public String publishPost(@PathVariable String postId,Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return postService.changePublishState(postId, user.getId(), true);
    }
    @PostMapping("/unpublish/{postId}")
    public String unPublishPost(@PathVariable String postId,Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return postService.changePublishState(postId, user.getId(), false);
    }

    @PutMapping("/like/{postId}")
    public String likePost(@PathVariable String postId,Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return postService.votePost(postId,user.getId(), VoteTypeEnum.LIKE);
    }

    @PutMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable String postId,Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return postService.votePost(postId,user.getId(), VoteTypeEnum.DISLIKE);
    }

}
