package com.example.product.repo;

import com.example.product.entity.BusinessProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBusinessRepository extends JpaRepository<BusinessProduct, Integer> {

  List<BusinessProduct> findAllByProductIdOrderByInDateAsc(int productId);

  List<BusinessProduct> findAllByProductId(int productId);

  boolean deleteAllByBatch(String batch);
}
