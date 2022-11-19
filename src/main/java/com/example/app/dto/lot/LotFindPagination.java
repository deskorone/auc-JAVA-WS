package com.example.app.dto.lot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotFindPagination {

    private List<LotPageFindDto> lotPageFindDtos;

    private List<Integer> totalCounts;


}