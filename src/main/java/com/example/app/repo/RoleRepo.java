package com.example.app.repo;

import com.example.app.models.Role;
import com.example.app.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long>{

    Optional<Role> findByName(RoleEnum roleEnum);
}