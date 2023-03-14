package com.accountbook.phoenix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRequest {

    private  String email;
    private String username;
    private  String password;
}
