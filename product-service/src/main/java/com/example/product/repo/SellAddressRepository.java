package com.example.product.repo;

import com.example.product.entity.SellAddressInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellAddressRepository extends JpaRepository<SellAddressInfo, Integer> {
  List<SellAddressInfo> findAllByCusId(int cusId);
  List<SellAddressInfo> findAllByBatch(String batch);
  Page<SellAddressInfo> findAll(Specification<SellAddressInfo> specification, Pageable pageable);
}
