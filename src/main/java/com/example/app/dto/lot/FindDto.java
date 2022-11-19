package com.example.app.dto.lot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindDto {
    private FindEnum findEnum;
    private String text;
}

