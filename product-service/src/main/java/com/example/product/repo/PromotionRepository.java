package com.example.product.repo;

import com.example.product.entity.Promotion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

  List<Promotion> findAllByCodeAndStatus(String code, String status);
}
