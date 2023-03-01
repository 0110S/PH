package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.FriendRequestDto;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserRepository userRepository;

    private final Utils utils;


    public ResponseEntity<MessageResponse> addFriend(FriendRequestDto friendRequestDto) {
        log.info("hit the method");
        try {
            Optional<User> user = userRepository.findById(friendRequestDto.getUserId());
            if (user.isEmpty()) {
                log.info(" exception");
                throw new InvalidUserException("user not found ");
            }
            log.info("in the method");

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setUserId(friendRequestDto.getUserId());
            friendRequest.setUser(utils.getUser());
            friendRequestRepository.save(friendRequest);
            return ResponseEntity.ok(new MessageResponse(true, friendRequest));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, friendRequestDto.getUserId()));
        }
    }


    public ResponseEntity<MessageResponse> listOfFriends() {
        try {
            List<FriendRequest> following = friendRequestRepository.findByUser(utils.getUser());
            if (following.isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(true, " no followers found "));
            }
            return ResponseEntity.ok(new MessageResponse(true, following));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();

        }
    }

    public ResponseEntity<MessageResponse> unfriend(FriendRequestDto friendRequestDto) {
        try {
            Optional<User> user = userRepository.findById(friendRequestDto.getUserId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found");
            }
            Optional<FriendRequest> friendRequest = friendRequestRepository.findById(friendRequestDto.getUserId());

            if (friendRequest.isEmpty()) {
                throw new InvalidUserException("user not found");
            }
            friendRequestRepository.delete(friendRequest.get());
            return ResponseEntity.ok(new MessageResponse(true, "deleted successfully" + friendRequest));
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "user not found"));
        }
    }
}
