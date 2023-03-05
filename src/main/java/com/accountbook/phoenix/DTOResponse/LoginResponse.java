package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.DTO.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private String accessToken;
    private UserData userResponse;
}
