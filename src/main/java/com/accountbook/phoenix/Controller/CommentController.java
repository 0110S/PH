package com.accountbook.phoenix.Controller;


import com.accountbook.phoenix.DTO.CommentRequest;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> addCommentToPost(@RequestBody CommentRequest commentRequest) {
        return commentService.addCommentToPost(commentRequest);
    }

    @PostMapping("/comment")
    public ResponseEntity<MessageResponse> addCommentToComment(@RequestBody CommentRequest commentRequest) {
        return commentService.addCommentToComment(commentRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> deleteComment(@RequestParam ("postId") int postId,@RequestParam ("commentId") int commentId){
        return commentService.deleteComment(postId,commentId);
    }
}
