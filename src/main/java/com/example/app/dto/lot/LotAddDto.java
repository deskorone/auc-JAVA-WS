package com.example.app.dto.lot;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class LotAddDto {

    @NotEmpty(message = "hello")
    private String lotName;

    @NotEmpty(message = "hello")
    @Positive
    @NumberFormat
    private String startPrice;

    @NotEmpty(message = "ds")
    private String desc;

    @NotNull
    private MultipartFile photo;


    public LotAddDto() {
    }
}
