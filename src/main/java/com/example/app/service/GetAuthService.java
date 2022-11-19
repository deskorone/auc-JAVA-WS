package com.example.app.service;


import com.example.app.models.UserSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetAuthService {

    public UserSecurity getUser(){
        return  (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
