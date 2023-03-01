package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.Service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/like")
    public ResponseEntity<MessageResponse> likePost(@RequestParam("postId") int postId) {
        return reactionService.likePost(postId);
    }

    @PostMapping("/dislike")
    private ResponseEntity<MessageResponse> dislikePost(@RequestParam("postId") int postId) {
        return reactionService.disLikePost(postId);
    }
}
