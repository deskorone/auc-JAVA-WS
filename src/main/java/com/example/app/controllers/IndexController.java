package com.example.app.controllers;


import com.example.app.dto.lot.LotPageFindDto;
import com.example.app.models.UserSecurity;
import com.example.app.service.LotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController{

    private final LotServiceImpl lotService;


    @Autowired
    public IndexController(LotServiceImpl lotService) {
        this.lotService = lotService;
    }

    @PostMapping("/home")
    public String getHome(){
        return "redirect:/";
    }


    @GetMapping
    public String getIndex(Model model, @AuthenticationPrincipal UserSecurity userSecurity){
        model.addAttribute("lots", lotService.getMainPage(userSecurity));
        return "index";
    }


}
