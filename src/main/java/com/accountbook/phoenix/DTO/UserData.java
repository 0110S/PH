package com.accountbook.phoenix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private String firstName;
    private String lastName;
    private String userName;
    private String mobileNumber;
    private String email;
    private File profilePic;
    private  boolean follow;
}

