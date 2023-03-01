package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.PostRequest;
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
    public ResponseEntity<MessageResponse> postSomething(@RequestBody PostRequest postRequest) {
        return postService.postSomething(postRequest);
    }

    @DeleteMapping("/post/delete")
    ResponseEntity<MessageResponse> deletePost(@RequestParam("postId") int id) {
        return postService.deletePost(id);
    }

}
