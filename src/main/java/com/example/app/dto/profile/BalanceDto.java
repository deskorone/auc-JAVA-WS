package com.example.app.dto.profile;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class BalanceDto {

    @NumberFormat
    @Positive
    @NotNull
    private Long balance;

}
