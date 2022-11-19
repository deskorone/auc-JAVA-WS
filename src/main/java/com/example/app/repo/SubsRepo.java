package com.example.app.repo;

import com.example.app.models.Lot;
import com.example.app.models.Subscribes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Optional;
import java.util.Set;

public interface SubsRepo extends JpaRepository<Subscribes, Long> {


    @Modifying
    @Query(value = "insert into subs_table (subs_id, lot_id) values (:sub_id, :lot_id)", nativeQuery = true)
    void sub(@Param("sub_id") Long subId, @Param("lot_id") Long lotId);

    @Modifying
    @Query(nativeQuery = true, value = "delete from subs_table where subs_id = :subs and lot_id = :lot")
    void unSub(@Param("subs") Long subs, @Param("lot") Long lot_id);



}
