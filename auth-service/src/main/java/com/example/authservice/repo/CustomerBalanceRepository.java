package com.example.authservice.repo;

import com.example.authservice.entities.user.CustomerBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerBalanceRepository extends JpaRepository<CustomerBalance, Integer> {
  Optional<CustomerBalance> findByCustomerId(int cusId);
}
