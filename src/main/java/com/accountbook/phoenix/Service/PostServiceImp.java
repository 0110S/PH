package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    private final Utils utils;

    @Override
    public ResponseEntity<MessageResponse> postSomething(PostRequest postRequest) {
        try {
            Post post = new Post();
            post.setPost(postRequest.getPost());
            post.setUser(utils.getUser());
            if (post.getUser() == null) {
                throw new InvalidUserException("user not found ");
            }
            postRepository.save(post);
            log.info(" " + post);
            return ResponseEntity.ok(new MessageResponse(true, post));
        } catch (InvalidUserException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deletePost(int id) {
        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post Not found ");
            }

            if (post.get().getUser().getId() != utils.getUser().getId()) {
                throw new InvalidUserException("user has no authority to delete post");
            }
            log.info(post.get() + "post ");
            postRepository.delete(post.get());
            return ResponseEntity.ok(new MessageResponse(true, "deleted successfully! " + post.get()));
        } catch (PostNotFoundException | InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(true, "post not found with  id :" + id));
        }
    }


}