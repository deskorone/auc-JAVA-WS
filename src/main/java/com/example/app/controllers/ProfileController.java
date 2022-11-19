package com.example.app.controllers;


import com.example.app.dto.lot.LotAddDto;
import com.example.app.dto.lot.LotPagination;
import com.example.app.dto.profile.BalanceDto;
import com.example.app.models.Profile;
import com.example.app.models.UserSecurity;
import com.example.app.repo.ProfileRepo;
import com.example.app.service.GetAuthService;
import com.example.app.service.LotServiceImpl;
import com.example.app.service.ProfileService;
import com.example.app.service.auth.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ProfileController {



    private final LotServiceImpl lotService;

    private final GetAuthService getAuthService;

    private final ProfileService profileService;

    private final RegistrationService registrationService;

    private final ProfileRepo profileRepo;

    @Autowired
    public ProfileController(LotServiceImpl lotService, GetAuthService getAuthService, ProfileService profileService, RegistrationService registrationService, ProfileRepo profileRepo) {
        this.lotService = lotService;
        this.getAuthService = getAuthService;
        this.profileService = profileService;
        this.registrationService = registrationService;
        this.profileRepo = profileRepo;
    }

    @GetMapping("/profile")
    public String getProfile(){
        return "profile";
    }


    @GetMapping("/profile/add")
    public String getAddPage(@ModelAttribute(name = "lotDto") LotAddDto lotDto){
        return "addLot";
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/profile/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addLot(@Valid @ModelAttribute(name = "lotDto") LotAddDto lotDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "addLot";
        }
        lotService.addLot(lotDto);
        return "addLot";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile/mylots")
    public String getMyLots(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int currentSize = 14;
        LotPagination lotPagination = lotService.getMyLotsPagination(currentPage, currentSize);
        model.addAttribute("lots", lotPagination.getLots());
        model.addAttribute("pageNumbers", lotPagination.getTotalCount());
        model.addAttribute("currentPage", currentPage);
        return "myLots";
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/profile/sale")
    public String saleLot(@RequestParam Long id, @AuthenticationPrincipal UserSecurity userSecurity){
        lotService.endLot(id, userSecurity);
        return "profile";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile/balance")
    public String getBalance(@ModelAttribute("balanceDto") BalanceDto balanceDto, @AuthenticationPrincipal UserSecurity userSecurity, Model model){
        model.addAttribute("balance", profileRepo.balance(userSecurity.getProfile().getId()));
        return "balance";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/profile/balance")
    public String addBalance(@Valid @ModelAttribute("balanceDto") BalanceDto balanceDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserSecurity userSecurity){
        if(bindingResult.hasErrors()){
            return "balance";
        }
        profileService.addBalance(balanceDto, userSecurity);
        return "redirect:/profile";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile/buys")
    public String getBuys(Model model, @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        int currentSize = 14;
        LotPagination lotPagination = lotService.getBuysPagination(currentPage, currentSize);
        model.addAttribute("lots", lotPagination.getLots());
        model.addAttribute("pageNumbers", lotPagination.getTotalCount());
        model.addAttribute("currentPage", currentPage);
        return "buys";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin(Model model, @RequestParam("page")Optional<Integer> page){
        int currentPage = page.orElse(1);
        int currentSize = 14;
        LotPagination lotPagination = lotService.getAdminPanel(currentPage, currentSize);
        model.addAttribute("lots", lotPagination.getLots());
        model.addAttribute("pageNumbers", lotPagination.getTotalCount());
        model.addAttribute("currentPage", currentPage);
        return "admin";
    }


}
