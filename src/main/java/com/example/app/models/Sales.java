package com.example.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "sales")
    private Profile profile;

    @OneToMany(mappedBy = "sales", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lot> lots;

}
