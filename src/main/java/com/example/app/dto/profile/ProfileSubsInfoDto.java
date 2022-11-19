package com.example.app.dto.profile;


import com.example.app.models.Lot;
import com.example.app.models.UserSecurity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSubsInfoDto {

    private Long id;

    private Long balance;

    private List<Long> subscribes;

    public static ProfileSubsInfoDto build(UserSecurity userSecurity){
        return null;
//        return new ProfileSubsInfoDto(userSecurity.getId(), userSecurity.getProfile().getBalance(),
//                userSecurity.getProfile()
//                        .getSubscribes()
//                        .stream()
//                        .map(Lot::getId)
//                        .collect(Collectors.toList())
//        );
    }

}
