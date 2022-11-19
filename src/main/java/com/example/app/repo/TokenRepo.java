package com.example.app.repo;

import com.example.app.models.ConfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<ConfToken, Long> {

    @Query(value = "select * from confirmation_token c where c.token=:token", nativeQuery = true)
    ConfToken getToken(@Param("token")String token);

}
