package com.example.app.repo;


import com.example.app.models.Lot;
import com.example.app.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LotRepo extends JpaRepository<Lot, Long> {

    Page<Lot> findByProfile(Profile profile, Pageable pageable);

    Optional<Lot> findById(@Param("id") Long id);


    @Query(nativeQuery = true, value = "SELECT * FROM lot l where l.lot_status = 'ACTIVE' limit(14)")
    List<Lot> getMainPage();

    @Query(value = "select l.* from lot l inner join subs_table as st on st.lot_id = l.id where st.subs_id = :id", nativeQuery = true)
    Set<Lot> getSubs(@Param("id") Long id);

    @Query(value = "select * from lot l where l.name like %:name% ", nativeQuery = true)
    Page<Lot> byNameFinder(@Param("name") String name, Pageable pageable);

    @Query(value = "select l.id, l.name, l.photo, l.price, (select exists (select * from subs_table s where s.subs_id = :user_id and s.lot_id = l.id )) as is_sub  from lot l where l.name like %:name%", nativeQuery = true)
    Page<Object[]> byNameFinderAuth(@Param("name") String name, @Param("user_id")  Long userId, Pageable pageable);

    @Query(value = "select * from lot l where l.description like %:desc% ", nativeQuery = true)
    Page<Lot> byDescriptionFinder(@Param("desc") String desc, Pageable pageable);

    @Query(value = "select * from lot l where l.date like %:date%", nativeQuery = true)
    Page<Lot> byDateFinder(@Param("date") String date, Pageable pageable);


    @Query(value = "delete from lot l where l.id = :id returning *", nativeQuery = true)
    Lot deleteByID(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value =  "delete from comment where lot_id = :id", nativeQuery = true)
    void deleteByComment(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from subs_table where lot_id = :id  ", nativeQuery = true)
    void deleteFromSubs(@Param("id") Long id);

    @Query(value = "select * from lot l where l.sales_id = :id ", nativeQuery = true)
    Page<Lot> getBuys(@Param("id") Long id, Pageable pageable);




    @Query(value = "select * from lot  l", nativeQuery = true)
    Page<Lot> getAdminPanel(Pageable pageable);

    @Query(value = "update lot set lot_status='CLOSE' where id = :id returning *", nativeQuery = true)
    Lot closeLot(@Param("id") Long id);

}
