package com.example.app.models;


import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "subs")
@Data
public class Subscribes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH
    })
    @JoinTable(name = "subs_table", joinColumns = @JoinColumn(name = "subs_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "lot_id", referencedColumnName = "id"))
    private Set<Lot> lots = new HashSet<>();


    @OneToOne(mappedBy = "subscribes")
    private Profile profile;


    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }


    @Transactional
    public void removeLot(Lot lot){
        this.getLots().remove(lot);
        lot.getSubscribes().remove(this);
    }


    public Subscribes() {
    }
}
