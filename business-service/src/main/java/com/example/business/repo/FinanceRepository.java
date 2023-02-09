package com.example.business.repo;

import com.example.business.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceRepository extends JpaRepository<Finance, Integer> {
  Finance findTopByOrderByIdDesc();
  Finance findTopByUpdateTimeIsLessThanOrderByIdDesc(long updateTime);
}
