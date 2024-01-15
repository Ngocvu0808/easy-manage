package com.example.product.filter;

import com.example.product.entity.SellAddressInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SellAddressInfoFilter extends EntityFilter<SellAddressInfo> {
  public Specification<SellAddressInfo> filter(long startDate, long endDate) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Predicate status0 = criteriaBuilder.equal(root.get("status"), '0');
      Predicate status1 = criteriaBuilder.equal(root.get("status"), '1');
      predicates.add(criteriaBuilder.or(status0, status1));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
