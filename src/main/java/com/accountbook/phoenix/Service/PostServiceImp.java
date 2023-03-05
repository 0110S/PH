package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTO.PostResponse;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.PostRepository;
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
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    private final Utils utils;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<PostResponse> postSomething(PostRequest postRequest) {
        try {
            Post post = new Post();
            post.setPost(postRequest.getPost());
            post.setUser(utils.getUser());
            if (post.getUser() == null) {
                throw new InvalidUserException("user not found ");
            }


            UserData userData = new UserData();
            userData.setFirstName(post.getUser().getFirstName());
            userData.setLastName(post.getUser().getLastName());
            userData.setMobileNumber(post.getUser().getMobileNumber());
            userData.setEmail(post.getUser().getEmail());
            userData.setUserName(post.getUser().getUsername());

            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(post.getPost());
            postResponse.setStatus("posted Successfully ");
            postResponse.setPost(post.getPost());
            postResponse.setUserData(userData);

            postRepository.save(post);
            log.info(" " + post);
            return ResponseEntity.ok(postResponse);
        } catch (InvalidUserException exception) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(" error ");
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<PostResponse> deletePost(int id) {
        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post Not found ");
            }

            if (post.get().getUser().getId() != utils.getUser().getId()) {
                throw new InvalidUserException("user has no authority to delete post");
            }

            UserData userData = new UserData();
            userData.setFirstName(post.get().getUser().getFirstName());
            userData.setLastName(post.get().getUser().getLastName());
            userData.setMobileNumber(post.get().getUser().getMobileNumber());
            userData.setEmail(post.get().getUser().getEmail());
            userData.setUserName(post.get().getUser().getUsername());
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(post.get().getPost());
            postResponse.setStatus("deleted Successfully ");

            postRepository.delete(post.get());
            return ResponseEntity.ok(postResponse);
        } catch (PostNotFoundException | InvalidUserException e) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("post not found");
            return ResponseEntity.badRequest().body(postResponse);
        }
    }

    @Override
    public ResponseEntity<PostResponse> fetchPostById(int id) {
        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found");
            }
            if (id != utils.getUser().getId()) {
                throw new InvalidUserException("user not found");
            }

            UserData userData = new UserData();
            userData.setFirstName(post.get().getUser().getFirstName());
            userData.setLastName(post.get().getUser().getLastName());
            userData.setMobileNumber(post.get().getUser().getMobileNumber());
            userData.setEmail(post.get().getUser().getEmail());
            userData.setUserName(post.get().getUser().getUsername());
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(post.get().getPost());
            postResponse.setStatus(" post found  ");
            postResponse.setPost(String.valueOf(post.get()));
            return ResponseEntity.ok(postResponse);
        } catch (InvalidUserException exception) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("user not found");
            return ResponseEntity.badRequest().body(postResponse);
        } catch (PostNotFoundException userException) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("post not found");
            return ResponseEntity.badRequest().body(postResponse);
        }
    }

    @Override
    public ResponseEntity<PostResponse> getAllPosts() {
        try {
            Optional<User> user = userRepository.findById(utils.getUser().getId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found ");
            }
            List<Object> posts = postRepository.findAllByUser(user.get());

            UserData userData = new UserData();
            userData.setFirstName(user.get().getFirstName());
            userData.setUserName(user.get().getUsername());
            userData.setLastName(user.get().getLastName());
            userData.setEmail(user.get().getEmail());
            userData.setMobileNumber(user.get().getMobileNumber());
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(posts.toString());
            postResponse.setUserData(userData);
            postResponse.setStatus("posts  ");
            return ResponseEntity.ok(postResponse);
        } catch (InvalidUserException exception) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("error");
            return ResponseEntity.badRequest().body(postResponse);
        }
    }
}