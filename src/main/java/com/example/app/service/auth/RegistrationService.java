package com.example.app.service.auth;


import com.example.app.dto.RegDto;
import com.example.app.models.*;
import com.example.app.repo.RoleRepo;
import com.example.app.repo.TokenRepo;
import com.example.app.repo.UserRepo;
import com.example.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserRepo usersRepo;
    private final RoleRepo rolesRepo;
    private final TokenRepo tokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final String confUrl = "http://localhost:8080/auth/%s";

    @Autowired
    public RegistrationService(UserRepo usersRepo, RoleRepo rolesRepo, TokenRepo tokenRepo, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.rolesRepo = rolesRepo;
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean registerUser(RegDto regDto) {
        Optional<UserSecurity> userSecurity = usersRepo.findByEmail(regDto.getEmail());
        if (userSecurity.isPresent()) {
            return false;
        }
        UserSecurity user = new UserSecurity(regDto.getEmail(), passwordEncoder.encode(regDto.getPassword()), regDto.getUsername(), true, false);
        Role role = rolesRepo.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("ROlE NOT FOUND"));
        if (regDto.getIsAdmin() == 1) {
            Role admin = rolesRepo.findByName(RoleEnum.ROLE_ADMIN).orElseThrow();
            user.addRole(admin);
        };
        user.addRole(role);
        Profile profile = new Profile();
        Subscribes subscribes = new Subscribes();
        Sales sales = new Sales();
        profile.setSubscribes(subscribes);
        profile.setSales(sales);
        profile.setBalance(10000L);
        user.setProfile(profile);
        UserSecurity userSec = usersRepo.save(user);
        try {
            ConfToken confirmationToken = new ConfToken(userSec,
                    UUID.randomUUID().toString(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1L));
            tokenRepo.save(confirmationToken);
            emailService.sendEmail(
                    userSec.getEmail(),
                    String.format(confUrl, confirmationToken.getToken()),
                    "Подтверждение электронной почты");
        } catch (Exception e) {
            usersRepo.delete(userSec);
            return false;
        }
        return true;
    }


    public boolean checkToken(String token) {
        System.out.println(token);
        ConfToken confirmationToken = tokenRepo.getToken(token);
        System.out.println(confirmationToken);
        if (confirmationToken == null || confirmationToken.getConfirmedAt() != null || confirmationToken.getExpired().isBefore(LocalDateTime.now())) {
            return false;
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        UserSecurity userSecurity = confirmationToken.getUserSecurity();
        userSecurity.setEnable();
        usersRepo.save(userSecurity);
        tokenRepo.save(confirmationToken);
        return true;
    }


}
