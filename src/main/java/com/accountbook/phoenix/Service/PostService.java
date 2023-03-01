package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.PostRequest;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<MessageResponse> postSomething(PostRequest postRequest);

    ResponseEntity<MessageResponse> deletePost(int id);



}
