package com.example.app.dto.lot;


import com.example.app.models.Lot;
import com.example.app.models.Profile;
import com.example.app.models.UserSecurity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotPageFindDto {

    private Long id;

    private String photo;

    private String name;

    private Long price;

    private boolean isSub;


    public static LotPageFindDto build(Lot lot, UserSecurity userSecurity){
        if(userSecurity == null){
            return new LotPageFindDto(lot.getId(), lot.getPhoto(), lot.getName(), lot.getPrice(), false);
        }else {
            try {
                Set<Lot> s = userSecurity.getProfile().getSubscribes().getLots();
                boolean isSub = s.stream().anyMatch(e -> e.getId().equals(lot.getId()));
                return new LotPageFindDto(lot.getId(), lot.getPhoto(), lot.getName(), lot.getPrice(),
                        isSub);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
//            return new LotPageFindDto(lot.getId(), lot.getPhoto(), lot.getName(), lot.getPrice(), false);
        }

    }



}
