package com.example.app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class RegDto {
    public RegDto() {
    }

    @NotEmpty
    private String username;

    @NotEmpty(message = "User's email cannot be empty.")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 4, max = 30)
    private String password;


    private  int isAdmin;


}
