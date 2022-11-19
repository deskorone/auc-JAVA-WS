package com.example.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "trading_history_id")
    private TradingHistory tradingHistory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserSecurity userSecurity;

    private String text;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;


    public Trade() {
    }

    public Trade(TradingHistory tradingHistory, UserSecurity userSecurity, String text, LocalDateTime time) {
        this.tradingHistory = tradingHistory;
        this.userSecurity = userSecurity;
        this.text = text;
        this.time = time;
    }
}
