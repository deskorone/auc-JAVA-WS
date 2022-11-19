package com.example.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
public class ConfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false,
            name = "user_id"
    )
    private UserSecurity userSecurity;


    @Column(nullable = false)
    private String token;
    private LocalDateTime created;


    @Column(nullable = false)
    private LocalDateTime expired;
    private LocalDateTime confirmedAt;


    public ConfToken(UserSecurity userSecurity, String token, LocalDateTime created, LocalDateTime expired) {
        this.userSecurity = userSecurity;
        this.token = token;
        this.created = created;
        this.expired = expired;
    }

    public ConfToken() {
    }
}
