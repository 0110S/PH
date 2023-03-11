package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private File profilePic;
    private boolean following;
    private  int followingCount;
}
