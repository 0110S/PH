package com.accountbook.phoenix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

