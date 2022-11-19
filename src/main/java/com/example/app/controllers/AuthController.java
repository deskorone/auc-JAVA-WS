package com.example.app.controllers;


import com.example.app.dto.RegDto;
import com.example.app.service.auth.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AuthController {

    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginForm(){
        return "auth/login";
    }

    @GetMapping("/reg")
    public String regForm(RegDto regDto, Model model){
        model.addAttribute("present", false);
        return "auth/reg";
    }

    @PostMapping("/reg")
    public String reg(@Valid @ModelAttribute(value = "regDto") RegDto regDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "auth/reg";
        }
        if(!registrationService.registerUser(regDto)){
            return "auth/reg";
        }
        return "redirect:/login";
    }

    @GetMapping("/auth/{token}")
    public String conf(Model model, @PathVariable("token") String token){
        model.addAttribute("check", registrationService.checkToken(token));
        return  "confirm";
    }


}
