package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.FriendRequestDto;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
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
    public ResponseEntity<?> addFriend(@RequestBody FriendRequestDto friendRequestDto) {
        return friendRequestService.addFriend(friendRequestDto);
    }

    @GetMapping("/friends")
    public ResponseEntity<MessageResponse> getALlFriends() {
        return friendRequestService.listOfFriends();
    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<MessageResponse> unfriend(@RequestBody FriendRequestDto friendRequestDto){
        return friendRequestService.unfriend(friendRequestDto);
    }
}
