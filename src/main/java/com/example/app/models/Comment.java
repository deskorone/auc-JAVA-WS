package com.example.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {


    public Comment(UserSecurity user, Lot lot, String commentText) {
        this.user = user;
        this.lot = lot;
        this.commentText = commentText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserSecurity user;

    @ManyToOne
    private Lot lot;

    private String commentText;
}
