package com.example.product.repo;

import com.example.product.entity.CartEntities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntities, Integer> {
  Optional<CartEntities> findByCustomerIdAndDeleted(int cusId, int deleted);
}
