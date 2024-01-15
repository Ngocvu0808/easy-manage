package com.example.product.repo;

import com.example.product.entity.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReportRepository extends JpaRepository<ProductReport, Integer> {
  ProductReport findTopByTimeIsLessThanOrderByIdDesc(long time);

  //findTopByUpdateTimeIsLessThanOrderByIdDesc
}
