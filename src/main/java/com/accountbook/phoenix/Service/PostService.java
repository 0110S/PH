package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTO.PostResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<PostResponse> postSomething(PostRequest postRequest);

    ResponseEntity<PostResponse> deletePost(int id);


    ResponseEntity<PostResponse> fetchPostById(int id);

    ResponseEntity<PostResponse> getAllPosts();
}
