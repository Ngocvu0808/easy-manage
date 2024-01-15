package com.example.product.repo;


import com.example.product.entity.CartDetailEntities;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetailEntities, Integer> {
  List<CartDetailEntities> findAllByCartIdAndDeleted(int cartId, int deleted);
}
