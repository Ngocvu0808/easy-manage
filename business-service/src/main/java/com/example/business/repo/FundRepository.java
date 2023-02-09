package com.example.business.repo;

import com.example.business.entity.FundHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<FundHistory, Integer> {

}
