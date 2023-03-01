package com.example.product.repo;

import com.example.product.entity.BusinessProduct;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBusinessRepository extends JpaRepository<BusinessProduct, Integer> {

  List<BusinessProduct> findAllByProductIdAndAvailableIsNotOrderByInDateAsc(int productId, int available);

  List<BusinessProduct> findAllByProductId(int productId);
  List<BusinessProduct> findAllByProductIdIn(Collection<Integer> productIds);

  boolean deleteAllByBatch(String batch);
}
