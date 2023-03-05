package com.accountbook.phoenix.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "first name should not be empty")
    private String firstName;
    @NotBlank(message = "last name should not be empty")
    private String lastName;
    @NotBlank(message = "username should not be empty")
    private String userName;
    @NotBlank(message = "mobile number should not be empty")
    private String mobileNumber;
    @Email(message = "enter the proper email")
    private String email;
    @NotBlank
    private String password;

    private File file;
}
