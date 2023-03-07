package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.DTO.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
     private  String status;
     private  String post;
     private UserData userData;
}
