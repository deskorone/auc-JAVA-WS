package com.example.app.repo;

import com.example.app.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SalesRepo extends JpaRepository<Sales, Long> {
}
