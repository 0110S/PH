package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.NotificationResponse;
import com.accountbook.phoenix.DTOResponse.PostData;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.Reaction;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.PostRepository;
import com.accountbook.phoenix.Repository.ReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final PostRepository postRepository;

    private final ReactionsRepository reactionsRepository;

    private final Utils utils;

    public ResponseEntity<NotificationResponse> likePost(int postId) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found");
            }
            User user = utils.getUser();
            if (user.getId() == 0) {
                throw new InvalidUserException("user not found");
            }
            Reaction reaction = new Reaction();
            reaction.setLike(+1);
            reaction.setPost(post.get());
            reaction.setUser(utils.getUser());
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setMessage("liked SuccessFully");

            UserData userData = new UserData();
            userData.setFirstName(post.get().getUser().getFirstName());
            userData.setLastName(post.get().getUser().getLastName());
            userData.setMobileNumber(post.get().getUser().getMobileNumber());
            userData.setUserName(post.get().getUser().getUsername());
            userData.setEmail(post.get().getUser().getEmail());

            PostData postData = new PostData();
            postData.setPost(post.get().getPost());
            postData.setPostData(userData);
            notificationResponse.setPost(postData);


            reactionsRepository.save(reaction);
            return ResponseEntity.ok(notificationResponse);
        } catch (PostNotFoundException e) {
            return  null;
        } catch (InvalidUserException exception) {
            return  null;
        }
    }

    public ResponseEntity<NotificationResponse> disLikePost(int postId) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found");
            }
            User user = utils.getUser();
            if (user.getId() == 0) {
                throw new InvalidUserException("user not found");
            }

            Reaction reaction = new Reaction();
            reaction.setDislike(+1);
            reaction.setPost(post.get());
            reaction.setUser(user);
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setMessage("disliked SuccessFully");

            UserData userData = new UserData();
            userData.setFirstName(post.get().getUser().getFirstName());
            userData.setLastName(post.get().getUser().getLastName());
            userData.setMobileNumber(post.get().getUser().getMobileNumber());
            userData.setUserName(post.get().getUser().getUsername());
            userData.setEmail(post.get().getUser().getEmail());

            PostData postData = new PostData();
            postData.setPost(post.get().getPost());
            postData.setPostData(userData);
            notificationResponse.setPost(postData);


            reactionsRepository.save(reaction);
            return ResponseEntity.ok(notificationResponse);
        } catch (PostNotFoundException e) {
            return null;
        } catch (InvalidUserException exception) {
            return  null;
        }
    }
}
