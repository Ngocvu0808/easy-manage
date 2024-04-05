package com.example.business.repo;

import com.example.business.entity.FundHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<FundHistory, Integer> {
  List<FundHistory> findAllByUserId(int userId);
  List<FundHistory> findAllByUserIdIn(List<Integer> userIds);
}
