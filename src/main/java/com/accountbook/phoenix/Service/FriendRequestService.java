package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.FriendRequestDto;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.DTOResponse.UserResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserRepository userRepository;

    private final Utils utils;


    public ResponseEntity<UserResponse> addFriend(FriendRequestDto friendRequestDto) {
        log.info("hit the method");
        try {
            Optional<User> user = userRepository.findById(friendRequestDto.getUserId());
            if (user.isEmpty()) {
                log.info(" exception");
                throw new InvalidUserException("user not found ");
            }
            log.info("user  " + user.get());
            if (utils.getUser().getId() == user.get().getId()) {
                throw new InvalidUserException("user and friend can not be same");
            }
            log.info("in the method");

            UserData userData = new UserData();
            userData.setUserName(user.get().getUsername());
            userData.setFirstName(user.get().getFirstName());
            userData.setLastName(user.get().getLastName());
            userData.setEmail(user.get().getEmail());
            userData.setMobileNumber(user.get().getMobileNumber());
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage("following");
            userResponse.setResponse(userData);
            FriendRequest friendRequest = new FriendRequest();

            friendRequest.setFriend(user.get());

            friendRequest.setUser(utils.getUser());
            friendRequestRepository.save(friendRequest);
            return ResponseEntity.ok(userResponse);
        } catch (InvalidUserException exception) {
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage("user not found to be friend");
            return ResponseEntity.badRequest().body((userResponse));
        }
    }


    public ResponseEntity<MessageResponse> listOfFriends() {
        try {
            List<FriendRequest> following = friendRequestRepository.findByUser(utils.getUser());

            List<String> friendIds = following.stream()
                    .map(user->user.getFriend().getFirstName() +" "+user.getUser().getFirstName()+",")
                    .collect(Collectors.toList());

            if (friendIds.isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(true, "no followers found"));
            }
            return ResponseEntity.ok(new MessageResponse(true, friendIds));
//            return ResponseEntity.ok(new MessageResponse(true, following));
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
