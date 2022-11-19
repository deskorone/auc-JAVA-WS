package com.example.app.service;


import com.example.app.dto.message.Message;
import com.example.app.models.*;
import com.example.app.repo.LotRepo;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LotWebSocketService {

    private final LotRepo lotRepo;

    private final UserRepo userRepo;

    @Autowired
    public LotWebSocketService(LotRepo lotRepo, UserRepo userRepo) {
        this.lotRepo = lotRepo;
        this.userRepo = userRepo;
    }


    @Transactional
    public Message changePrice(Message message) {
        Lot lot = lotRepo.findById(message.getProductId()).orElseThrow(() -> new RuntimeException("Lot not found"));
        UserSecurity userSecurity = userRepo.findById(message.getUserId()).orElseThrow();

        if (!checkNumString(message.getValue()) || lot.getLotStatus().equals(LotStatusEnum.CLOSE)) {
            return new Message(message.getProductId(), lot.getPrice().toString(), StatusEnum.ERROR, message.getUserId(), lot.getName(), null, userSecurity.getName());
        }
        if (userSecurity.getProfile().getBalance() < Long.parseLong(message.getValue()) || Long.parseLong(message.getValue()) < lot.getPrice()) {
            return new Message(message.getProductId(), lot.getPrice().toString(), StatusEnum.ERROR, message.getUserId(), lot.getName(), null, userSecurity.getName());
        }
        if(Long.parseLong(message.getValue()) == lot.getPrice()){
            return new Message(message.getProductId(), lot.getPrice().toString(), StatusEnum.ERROR, message.getUserId(), lot.getName(), null, userSecurity.getName());
        }

        lot.setLastChanger(userSecurity.getProfile());
        lot.setPrice(Long.parseLong(message.getValue()));
        lotRepo.save(lot);
        TradingHistory tradingHistory = lot.getTradingHistory();
        Trade trade = new Trade(tradingHistory, userSecurity, "Поднял цену до " + message.getValue(), LocalDateTime.now());
        tradingHistory.getTrades().add(trade);
        System.out.println(userSecurity.getName());
        return new Message(message.getProductId(), message.getValue(), message.getStatus(), message.getUserId(), lot.getName(), LocalDateTime.now(), userSecurity.getName());
    }


    private boolean checkNumString(String num) {
        try {
            Long.parseLong(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
