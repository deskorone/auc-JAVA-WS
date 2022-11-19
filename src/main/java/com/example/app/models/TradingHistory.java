package com.example.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class TradingHistory {

    public TradingHistory() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trading_history_id")
    private List<Trade>trades;

    @OneToOne(mappedBy = "tradingHistory")
    private Lot lot;

    private LocalDateTime time;
}
