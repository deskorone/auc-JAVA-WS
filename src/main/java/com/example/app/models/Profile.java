package com.example.app.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Table(name = "profile")
public class Profile {

    public Profile() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long balance;

    @OneToOne(mappedBy = "profile")
    @JoinColumn(name = "user_id")
    private UserSecurity user;


    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private List<Lot> lots;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subs_id", referencedColumnName = "id")
    private Subscribes subscribes;




    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "lastChanger")
    private Lot myJoinLots;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sales_id", referencedColumnName = "id")
    private Sales sales;



    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
//                ", subscribes=" + subscribes +
                '}';
    }
}
