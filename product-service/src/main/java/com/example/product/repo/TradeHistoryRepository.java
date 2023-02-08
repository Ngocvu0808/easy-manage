package com.example.product.repo;

import com.example.product.entity.TradeHistory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Integer> {
  List<TradeHistory> findAllByBatch(String batch);
  Page<TradeHistory> findAll(Specification<TradeHistory> specification, Pageable pageable);
}
