package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<MessageResponse> postSomething(PostRequest postRequest);

    ResponseEntity<MessageResponse> deletePost(int id);


    ResponseEntity<MessageResponse> fetchPostById(int id);

    ResponseEntity<MessageResponse> getAllPosts() throws JsonProcessingException;

    ResponseEntity<MessageResponse> getAllFriendsPost();

//    ResponseEntity<MessageResponse> likePost(int postId);
}
