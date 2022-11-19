package com.example.app.dto.lot;

import com.example.app.models.Comment;
import com.example.app.models.Lot;
import com.example.app.models.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotResponse {

    private Lot lot;

    private List<Trade> trades;

    private boolean isMy;

    private List<Comment> comments;

}
