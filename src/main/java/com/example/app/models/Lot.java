package com.example.app.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "lot")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private String photo;

    private String name;
    private Long price;

    private LocalDateTime timeSale;

    private LocalDateTime date;


    @Enumerated(EnumType.STRING)
    private LotStatusEnum lotStatus;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE
    }, mappedBy = "lots")
    private Set<Subscribes> subscribes = new HashSet<>();


    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private List<Comment> comments;


    private Long startPrice;

    private String description;


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_changer_id")
    private Profile lastChanger;

    public Lot() {
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    private Sales sales;


    @OneToOne(cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "trading_history_id")
    private TradingHistory tradingHistory;

    @Override
    public String toString() {
        return "Lot{ id  " + id +
                "price=" + price +
                '}';
    }
}
