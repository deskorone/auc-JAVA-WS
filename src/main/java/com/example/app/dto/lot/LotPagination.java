package com.example.app.dto.lot;

import com.example.app.models.Lot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotPagination {

    private List<Lot> lots;

    private List<Integer> totalCount;

}
