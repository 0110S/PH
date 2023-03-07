//package com.accountbook.phoenix.Controller;
//
//import com.accountbook.phoenix.DTOResponse.MessageResponse;
//import com.accountbook.phoenix.DTOResponse.NotificationResponse;
//import com.accountbook.phoenix.Service.ReactionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/reaction")
//@CrossOrigin("*")
//
//public class ReactionController {
//
//    private final ReactionService reactionService;
//
//    @PostMapping("/like")
//    public ResponseEntity<NotificationResponse> likePost(@RequestParam("postId") int postId) {
//        return reactionService.likePost(postId);
//    }
//
//    @PostMapping("/dislike")
//    private ResponseEntity<NotificationResponse> dislikePost(@RequestParam("postId") int postId) {
//        return reactionService.disLikePost(postId);
//    }
//}
