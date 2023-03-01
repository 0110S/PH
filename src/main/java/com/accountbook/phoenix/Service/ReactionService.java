package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.Reaction;
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

    public ResponseEntity<MessageResponse> likePost(int postId) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found");
            }
            if (utils.getUser().getId() == 0) {
                throw new InvalidUserException("user not found");
            }
            Reaction reaction = new Reaction();
            reaction.setLike(+1);
            reaction.setPost(post.get());
            reaction.setUser(utils.getUser());
            reactionsRepository.save(reaction);
            return ResponseEntity.ok(new MessageResponse(true, reaction));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "post not found" + postId));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "user not found"));
        }
    }

    public ResponseEntity<MessageResponse> disLikePost(int postId) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found");
            }
            if (utils.getUser().getId() == 0) {
                throw new InvalidUserException("user not found");
            }

            Reaction reaction = new Reaction();
            reaction.setDislike(+1);
            reaction.setPost(post.get());
            reaction.setUser(utils.getUser());
            reactionsRepository.save(reaction);
            return ResponseEntity.ok(new MessageResponse(true, reaction));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "post not found" + postId));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "user not found"));
        }
    }
}
