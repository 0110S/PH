package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.DTO.UserData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String message;
    private UserData response;
}

