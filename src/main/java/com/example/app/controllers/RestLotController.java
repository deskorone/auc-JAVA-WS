package com.example.app.controllers;


import com.example.app.dto.message.TextDto;
import com.example.app.service.LotServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestLotController {


    private final LotServiceImpl lotService;

    public RestLotController(LotServiceImpl lotService) {
        this.lotService = lotService;
    }

    @GetMapping(value = "/photo/{path}", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> getPhoto(@PathVariable String path){
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
                .body(lotService.getPhoto(path));
    }


    @GetMapping(value = "/subs")
    public ResponseEntity<?> getSubs(){
        return ResponseEntity.ok(lotService.getSubs());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/sub/add")
    public ResponseEntity<?> addSub(@RequestParam("id") Long id){
        return ResponseEntity.ok(lotService.sub(id));
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/sub/add")
    public ResponseEntity<?> deleteSub(@RequestParam("id") Long id){
        lotService.unsub(id);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/lot/comment/{id}")
    public ResponseEntity<?> addComment(@PathVariable("id") Long id, @RequestBody TextDto textDto){
        System.out.println(textDto);

        return ResponseEntity.ok(lotService.addComment(textDto.getText(), id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/close/lot/{id}")
    public ResponseEntity<?> closeLot(@PathVariable("id") Long id){
        return new ResponseEntity<>(lotService.closeLot(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/lot/{id}")
    public ResponseEntity<?> deleteLot(@PathVariable("id") Long id){
        return new ResponseEntity<>(lotService.deleteLot(id));
    }
}
