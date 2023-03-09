package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.Service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
@CrossOrigin("*")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/addFriend")
    public ResponseEntity<?> addFriend(@RequestParam("receiverId") int receiverId) {
        return friendRequestService.followUser(receiverId);
    }

    @GetMapping("/friends")
    public ResponseEntity<MessageResponse> getALlFriends() {
        return friendRequestService.listOfFriends();
    }


}
