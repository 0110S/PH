package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.Service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
@CrossOrigin("*")
public class ReactionController {
    private final ReactionService reactionService;

        @PutMapping("toggleLike")
        ResponseEntity<?> toggleSwitch(@RequestParam("postId") int postId) {
        return reactionService.togLike(postId);
    }

}
