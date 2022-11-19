package com.example.app.repo;

import com.example.app.models.TradingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingHistoryRepo extends JpaRepository<TradingHistory, Long> {
}
