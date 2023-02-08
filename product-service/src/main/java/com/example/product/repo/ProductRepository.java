package com.example.product.repo;

import com.example.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

  Product findByCode(String code);

  List<Product> findAllByIdIn(Collection<Integer> ids);

  Page<Product> findAll(Specification<Product> specification, Pageable pageable);

}
