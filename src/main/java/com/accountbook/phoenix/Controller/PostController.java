package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTO.PostResponse;
import com.accountbook.phoenix.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class PostController {

    private final PostService postService;

    @PostMapping("post")
    public ResponseEntity<PostResponse> postSomething(@RequestBody PostRequest postRequest) {
        return postService.postSomething(postRequest);
    }

    @DeleteMapping("/post/delete")
    ResponseEntity<PostResponse> deletePost(@RequestParam("postId") int id) {
        return postService.deletePost(id);
    }

    @GetMapping("/getPost")
    ResponseEntity<PostResponse> getPost(@RequestParam ("postId") int id){
        return postService.fetchPostById(id);
    }

    @GetMapping("/allPosts")
    ResponseEntity<PostResponse> getAllPosts(){
        return postService.getAllPosts();
    }
}
