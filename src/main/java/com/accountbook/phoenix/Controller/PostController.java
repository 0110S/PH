package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTOResponse.LikeDto;
import com.accountbook.phoenix.DTOResponse.PostResponse;
import com.accountbook.phoenix.Service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
@CrossOrigin("*")
public class PostController {

    private final PostService postService;

    @PostMapping("post")
    public ResponseEntity<PostResponse> postSomething(@RequestBody PostRequest postRequest) {
        return postService.postSomething(postRequest);
    }

    @DeleteMapping("/post/delete")
    ResponseEntity<?> deletePost(@RequestParam("postId") int id) {
        return postService.deletePost(id);
    }

    @GetMapping("/getPost")
    ResponseEntity<?> getPost(@RequestParam("postId") int id) {
        return postService.fetchPostById(id);
    }

    @PutMapping("toggleLike")
    ResponseEntity<?> toggleSwitch(@RequestParam("postId") int postId) {
        return postService.likePost(postId);
    }

    @GetMapping("/allPosts")
    ResponseEntity<String> getAllPosts() throws JsonProcessingException {
        return postService.getAllPosts();
    }

    @GetMapping("/getAllFriendsPost")
    ResponseEntity<?> getAllFriendsPost() {
        return postService.getAllFriendsPost();
    }
}
