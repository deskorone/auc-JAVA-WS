package com.example.app.repo;

import com.example.app.models.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepo extends JpaRepository<Trade, Long> {

    @Query(nativeQuery = true, value = "select * from trade t where t.trading_history_id = :id order by t.time asc")
    List<Trade> findTradesSortByProduct(@Param("id") Long historyId);


}
