package com.social.app.controller;

import com.social.app.dto.PostCreateDTO;
import com.social.app.dto.PostResponseDTO;
import com.social.app.model.PostModel;
import com.social.app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/")
    public PostResponseDTO createPost (@ModelAttribute PostCreateDTO newPost){
        PostModel dbPost =postService.createPost(newPost);
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
    public String deletePostById(@PathVariable String postId){
         postService.deletePostById(postId);
         return "Post Deleted";
    }

//    @PutMapping("/like/{postId}")
//    public String likePost(@PathVariable String postId){
//
//    }

}
