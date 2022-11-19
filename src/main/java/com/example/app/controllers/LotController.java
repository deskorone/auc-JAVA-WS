package com.example.app.controllers;


import com.example.app.dto.lot.*;
import com.example.app.models.Lot;
import com.example.app.models.UserSecurity;
import com.example.app.service.LotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class LotController {


    private final LotServiceImpl lotService;

    @Autowired
    public LotController(LotServiceImpl lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/lot/{id}")
    public String getLot(@PathVariable Long id, Model model, @AuthenticationPrincipal UserSecurity userSecurity) {
        if (userSecurity != null) {
            model.addAttribute("userId", userSecurity.getId());
        }
        LotResponse lotResponse = lotService.getLot(id, userSecurity);
        model.addAttribute("lot", lotResponse.getLot());
        model.addAttribute("isMy", lotResponse.isMy());
        model.addAttribute("trades", lotResponse.getTrades());
        model.addAttribute("comments", lotResponse.getComments());
        return "lot";
    }


    @PostMapping("/find")
    public String findLotPage(@ModelAttribute("findDto") FindDto findDto,
                              @RequestParam("page") Optional<Integer> page,
                              @AuthenticationPrincipal UserSecurity userSecurity, RedirectAttributes redirectAttributes) {
        return "redirect:/find";

    }

    @GetMapping("/find")
    public String findLot(Model model,
                          @RequestParam(name = "findEnum", required = false)String findEnum,
                          @RequestParam(name = "text", required = false)String text,
                          @RequestParam(name = "page") Optional<Integer> page,
                          @AuthenticationPrincipal UserSecurity userSecurity) {
        int currentPage = page.orElse(1);

        if (text == null || findEnum == null ) {
            return "findPage";
        }
        FindDto findDto = new FindDto();
        findDto.setText(text);
        try {
            findDto.setFindEnum(FindEnum.valueOf(findEnum));
        }catch (Exception e) {
            findDto.setFindEnum(FindEnum.BY_NAME);
        }
        try {
            model.addAttribute("text", text);
            model.addAttribute("findEnum", findEnum);
            LotFindPagination lotFindPagination = lotService.findBy(findDto, userSecurity, currentPage);
            model.addAttribute("lots", lotFindPagination.getLotPageFindDtos());
            model.addAttribute("pageNumbers", lotFindPagination.getTotalCounts());
            model.addAttribute("currentPage", currentPage);
        }catch (Exception e){
            e.printStackTrace();
            return "findPage";
        }
        return "findPage";

    }


}
