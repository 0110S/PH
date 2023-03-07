package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTOResponse.LikeDto;
import com.accountbook.phoenix.DTOResponse.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<PostResponse> postSomething(PostRequest postRequest);

    ResponseEntity<PostResponse> deletePost(int id);


    ResponseEntity<?> fetchPostById(int id);

    ResponseEntity<String> getAllPosts() throws JsonProcessingException;

    ResponseEntity<?> getAllFriendsPost();

    ResponseEntity<?> likePost(LikeDto likeDto);
}
