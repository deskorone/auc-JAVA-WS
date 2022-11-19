package com.example.app.service;


import com.example.app.dto.profile.BalanceDto;
import com.example.app.dto.profile.ProfileSubsInfoDto;
import com.example.app.models.Profile;
import com.example.app.models.UserSecurity;
import com.example.app.repo.ProfileRepo;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final GetAuthService getAuthService;

    private final UserRepo userRepo;

    private final ProfileRepo profileRepo;

    @Autowired
    public ProfileService(GetAuthService getAuthService, UserRepo userRepo, ProfileRepo profileRepo) {
        this.getAuthService = getAuthService;
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    public ProfileSubsInfoDto getSubsAndInfo(){
        UserSecurity userSecurity = getAuthService.getUser();
        if(userSecurity == null){
            return null;
        }
        return ProfileSubsInfoDto.build(userSecurity);
    }

    @Modifying
    @Transactional
    public void addBalance(BalanceDto balanceDto, UserSecurity userSecurity){
        Profile profile = userSecurity.getProfile();
        profileRepo.setBalance(profileRepo.balance(profile.getId()) + balanceDto.getBalance(), profile.getId());
        profile.setBalance(balanceDto.getBalance() + profile.getBalance());
    }


}
