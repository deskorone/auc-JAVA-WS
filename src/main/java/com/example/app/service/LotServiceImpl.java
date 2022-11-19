package com.example.app.service;

import com.example.app.dto.lot.*;
import com.example.app.dto.message.Message;
import com.example.app.models.*;
import com.example.app.repo.*;
import com.example.app.service.interfaces.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@Slf4j
public class LotServiceImpl implements LotService {

    private final LotRepo lotRepo;

    private final GetAuthService getAuthService;

    private final SubsRepo subsRepo;

    private final TradeRepo tradeRepo;

    private final ProfileRepo profileRepo;

    private final EmailService emailService;

    private final SalesRepo salesRepo;

    private final String saleLotMessage = "Лот с именем %s продан пользователю с именем %s";

    private final String subjectSale = "Здравствуйте, %s торги с лотом на который вы подписаны окончены";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final CommentRepo commentRepo;

    @Autowired
    public LotServiceImpl(LotRepo lotRepo, GetAuthService getAuthService, SubsRepo subsRepo, TradeRepo tradeRepo, ProfileRepo profileRepo, EmailService emailService, SalesRepo salesRepo, SimpMessagingTemplate simpMessagingTemplate, CommentRepo commentRepo) {
        this.lotRepo = lotRepo;
        this.getAuthService = getAuthService;
        this.subsRepo = subsRepo;
        this.tradeRepo = tradeRepo;
        this.profileRepo = profileRepo;
        this.emailService = emailService;
        this.salesRepo = salesRepo;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.commentRepo = commentRepo;
    }

    private String savePhoto(MultipartFile multipartFile) {
        if (multipartFile.getSize() == 0) {
            return null;
        }
        try {
            File photo = new File(String.format("src/main/resources/static/images/%s", UUID.randomUUID() + ".png"));
            if (!photo.exists()) {
                photo.createNewFile();
            }
            try (OutputStream outputStream = new FileOutputStream(photo)) {
                outputStream.write(multipartFile.getBytes());
            }
            return photo.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Lot addLot(LotAddDto lotAddDto) {
        Lot lot = new Lot();
        TradingHistory tradingHistory = new TradingHistory();
        lot.setTradingHistory(tradingHistory);
        lot.setName(lotAddDto.getLotName());
        lot.setProfile(getAuthService.getUser().getProfile());
        lot.setPhoto(savePhoto(lotAddDto.getPhoto()));
        lot.setPrice(Long.parseLong(lotAddDto.getStartPrice()));
        lot.setDescription(lotAddDto.getDesc());
        lot.setLotStatus(LotStatusEnum.ACTIVE);
        lot.setStartPrice(Long.parseLong(lotAddDto.getStartPrice()));
        lot.setDate(LocalDateTime.now());
        Lot lotl = lotRepo.save(lot);
        getAuthService.getUser().getProfile().getLots().add(lot);
        return lotl;
    }

    @Modifying
    @Transactional
    public HttpStatus closeLot(Long id) {
        try {
            lotRepo.closeLot(id);
            lotRepo.deleteFromSubs(id);
        } catch (Exception e) {
            log.warn("ERROR ClOSE");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }



    @Modifying
    @Transactional
    public HttpStatus deleteLot(Long id){
        try {
            lotRepo.deleteFromSubs(id);
            lotRepo.deleteByComment(id);
            lotRepo.deleteByID(id);
        }catch (Exception e){
            e.printStackTrace();
            return  HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }




    @Transactional
    @Override
    public CommentResponseDto addComment(String text, Long id) {
        UserSecurity userSecurity = getAuthService.getUser();
        Lot lot = lotRepo.findById(id).orElseThrow();
        Comment comment = new Comment(userSecurity, lot, text);
        lot.getComments().add(comment);
        return CommentResponseDto.build(comment);
    }

    @Override
    public LotPagination getMyLotsPagination(int currentPage, int currentSize) {
        Page<Lot> lots = lotRepo.findByProfile(getAuthService.getUser().getProfile(), PageRequest.of(currentPage - 1, currentSize));
        return new LotPagination(lots.toList(), getPageNumbers(lots.getTotalPages()));
    }

    public LotPagination getBuysPagination(int currentPage, int currentSize) {
        Page<Lot> lots = lotRepo.getBuys(getAuthService.getUser().getProfile().getSales().getId(), PageRequest.of(currentPage - 1, currentSize));
        ;
        return new LotPagination(lots.toList(), getPageNumbers(lots.getTotalPages()));
    }


    @Override
    public byte[] getPhoto(String path) {
        try {
            File file = new File((String.format("src/main/resources/static/images/%s", path)));
            try (InputStream stream = new FileInputStream(file)) {
                return stream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Long> getSubs() {
        return getAuthService
                .getUser()
                .getProfile()
                .getSubscribes()
                .getLots()
                .stream()
                .map(Lot::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LotResponse getLot(Long id, UserSecurity userSecurity) {
        Lot lot = lotRepo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find resource"));
        List<Trade> trades = tradeRepo.findTradesSortByProduct(lot.getTradingHistory().getId());
        boolean isMy;
        if (userSecurity == null) {
            isMy = false;
        } else {
            isMy = userSecurity.getProfile().getId().equals(lot.getProfile().getId());
        }
        return new LotResponse(lot, trades, isMy, commentRepo.getCommentsByLot(lot.getId()));
    }

    @Transactional
    @Override
    public Lot sub(Long id) {
        try {
            Subscribes subscribes = getAuthService.getUser().getProfile().getSubscribes();
            Lot lot = lotRepo.findById(id).orElseThrow();
            subsRepo.sub(subscribes.getId(), lot.getId());
            subscribes.setLots(lotRepo.getSubs(subscribes.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Modifying //что бы корректно работали запросы
    @Override
    @Transactional
    public Lot unsub(Long id) {
        try {

            Subscribes subscribes = getAuthService.getUser().getProfile().getSubscribes();
            Lot lot = lotRepo.findById(id).orElseThrow();
            System.out.println(subscribes.getLots());
            subsRepo.unSub(subscribes.getId(), lot.getId());
            subscribes.setLots(lotRepo.getSubs(subscribes.getId()));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    @Override
    public List<LotPageFindDto> getMainPage(UserSecurity userSecurity) {
        return lotRepo
                .getMainPage()
                .stream()
                .map(e -> LotPageFindDto.build(e, userSecurity))
                .collect(Collectors.toList());
    }

    @Modifying
    @Transactional
    @Override
    public boolean endLot(Long id, UserSecurity userSecurity) {
        System.out.println(userSecurity.getProfile().getLots());
        Lot lot = lotRepo.findById(id).orElseThrow();
        if (getAuthService.getUser().getProfile().getLots().stream().anyMatch(e -> e.getId().equals(id))) {
            if (lot.getLotStatus().equals(LotStatusEnum.ACTIVE) && lot.getLastChanger() != null) {
                saleLot(lot, lot.getLastChanger());
            }
        }

        return false;
    }

    @Override
    public LotFindPagination findBy(FindDto findDto, UserSecurity userSecurity, int page) {
        Pageable pageable = PageRequest.of(page - 1, 14);
        LotFindPagination lotFindPagination = new LotFindPagination();
        Page<Lot> lots;
        List<Integer> pages;
        switch (findDto.getFindEnum()) {
            case BY_NAME:
                lots = lotRepo.byNameFinder(findDto.getText(), pageable);
                pages = getPageNumbers(lots.getTotalPages());
                lotFindPagination.setTotalCounts(pages);
                lotFindPagination
                        .setLotPageFindDtos(lots
                                .stream()
                                .map(e -> LotPageFindDto.build(e, userSecurity))
                                .collect(Collectors.toList()));

                break;
            case BY_DESCRIPTION:
                lots = lotRepo.byDescriptionFinder(findDto.getText(), pageable);
                pages = getPageNumbers(lots.getTotalPages());
                lotFindPagination.setTotalCounts(pages);
                lotFindPagination
                        .setLotPageFindDtos(lots
                                .stream()
                                .map(e -> LotPageFindDto.build(e, userSecurity))
                                .collect(Collectors.toList()));

                break;
            default:
                throw new RuntimeException();


        }
        return lotFindPagination;

    }


    private void saleLot(Lot lot, Profile profile) {
        if (lot.getPrice() > profile.getBalance()) {
            return;
        }
        lot.setTimeSale(LocalDateTime.now());
        lot.setLotStatus(LotStatusEnum.CLOSE);

        lot.getSubscribes()
                .forEach(e -> {
                    emailService.sendEmail(
                            e.getProfile().getUser().getEmail(),
                            String.format(saleLotMessage, lot.getName(), e.getProfile().getUser().getName()),
                            String.format(subjectSale, e.getProfile().getUser().getName()));
                });
        emailService.sendEmail(lot.getLastChanger().getUser().getEmail(), "Поздравляем с покупкой", "Здравствуйте, "
                + lot.getLastChanger().getUser().getName() + " вы приобели лот " + lot.getName());
        lotRepo.deleteFromSubs(lot.getId());
        lot.setSales(profile.getSales());
        profile.setBalance(profile.getBalance() - lot.getPrice());
        Message message = new Message(lot.getId(),
                "Торги окончены",
                StatusEnum.END,
                null,
                lot.getName(),
                LocalDateTime.now());
        simpMessagingTemplate.convertAndSendToUser(lot.getId().toString(),"/private", message);
        lot.getProfile().setBalance(lot.getProfile().getBalance() + lot.getPrice());
        profileRepo.save(lot.getProfile());
        profileRepo.setBalance(profile.getBalance(), profile.getId());
        lotRepo.save(lot);System.out.println(lot.getPrice()  + "  " + profile.getBalance());
    }


    private List<Integer> getPageNumbers(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return List.of(1);
    }


    public LotPagination getAdminPanel(int currentPage, int currentSize) {

        Page<Lot> page = lotRepo.getAdminPanel(PageRequest.of(currentPage - 1, currentSize));
        return new LotPagination(page.toList(), getPageNumbers(page.getTotalPages()));
    }


}
