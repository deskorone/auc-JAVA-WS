package com.example.app.repo;

import com.example.app.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepo extends JpaRepository<Profile, Long> {

    @Modifying
    @Transactional
    @Query(value = "update profile set balance = :b where id=:id", nativeQuery = true)
    void setBalance(@Param("b") Long balance, @Param("id") Long id);


    @Query(value = "select balance from profile where id = :id", nativeQuery = true)
    Long balance(@Param("id") Long id);


}
