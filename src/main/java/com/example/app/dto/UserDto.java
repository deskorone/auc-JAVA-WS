package com.example.app.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @NotEmpty(message = "User's name cannot be empty")
    @Size(min = 5, max = 250)
    private String username;

    @NotEmpty(message = "Password is not empty")
    @Size(min = 4, max = 20)
    private String password;


}
