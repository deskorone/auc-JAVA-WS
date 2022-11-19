package com.example.app.repo;


import com.example.app.models.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserSecurity, Long> {


    Optional<UserSecurity> findByEmail(String email);

    Optional<UserSecurity> findById(Long id);


}
