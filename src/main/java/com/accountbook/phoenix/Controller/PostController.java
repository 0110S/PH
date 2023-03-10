package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
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
    public ResponseEntity<MessageResponse> postSomething(@RequestBody PostRequest postRequest) {
        return postService.postSomething(postRequest);
    }

    @DeleteMapping("/post/delete")
    ResponseEntity<MessageResponse> deletePost(@RequestParam("postId") int id) {
        return postService.deletePost(id);
    }

    @GetMapping("/getPost")
    ResponseEntity<MessageResponse> getPost(@RequestParam("postId") int id) {
        return postService.fetchPostById(id);
    }

    @PutMapping("toggleLike")
    ResponseEntity<MessageResponse> toggleSwitch(@RequestParam("postId") int postId) {
        return postService.likePost(postId);
    }

    @GetMapping("/allPosts")
    ResponseEntity<MessageResponse> getAllPosts() throws JsonProcessingException {
        return postService.getAllPosts();
    }

    @GetMapping("/getAllFriendsPost")
    ResponseEntity<?> getAllFriendsPost() {
        return postService.getAllFriendsPost();
    }
}
