package com.accountbook.phoenix.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
     private  String status;
     private  String post;
     private  UserData userData;
}
