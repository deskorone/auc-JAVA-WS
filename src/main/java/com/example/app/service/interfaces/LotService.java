package com.example.app.service.interfaces;

import com.example.app.dto.lot.*;
import com.example.app.models.Lot;
import com.example.app.models.UserSecurity;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface LotService {

    Lot addLot(LotAddDto lotAddDto);
    HttpStatus closeLot(Long id);
    CommentResponseDto addComment(String text, Long id);

    LotPagination getMyLotsPagination(int currentPage, int currentSize);

    byte[] getPhoto(String path);

    List<Long> getSubs();

    LotResponse getLot(Long id, UserSecurity userSecurity);

    Lot sub(Long id);

    Lot unsub(Long id);

    List<LotPageFindDto> getMainPage(UserSecurity userSecurity);

    boolean endLot(Long id, UserSecurity userSecurity);

    LotFindPagination findBy(FindDto findDto, UserSecurity userSecurity, int page);

}
