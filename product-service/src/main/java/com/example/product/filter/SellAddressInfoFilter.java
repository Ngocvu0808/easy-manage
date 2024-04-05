package com.example.product.filter;

import com.example.product.entity.SellAddressInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class SellAddressInfoFilter extends EntityFilter<SellAddressInfo> {
  public Specification<SellAddressInfo> filter(String search, String status) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isEmpty(status)) {
        Predicate status0 = criteriaBuilder.equal(root.get("status"), "0");
        Predicate status1 = criteriaBuilder.equal(root.get("status"), "1");
        Predicate status2 = criteriaBuilder.equal(root.get("status"), "2");
        predicates.add(criteriaBuilder.or(status0, status1, status2));
      } else {
        List<String> statusList = Arrays.asList(status.split(","));
        predicates.add(criteriaBuilder.in(root.get("status")).value(statusList));
      }

      if (!StringUtils.isEmpty(search)) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate cusName = criteriaBuilder.like(root.get("cusName"), searchValue.trim());
        Predicate phone = criteriaBuilder.like(root.get("phone"), searchValue.trim());
        predicates.add(criteriaBuilder.or(phone, cusName));
      }
      criteriaQuery.orderBy(criteriaBuilder.asc(root.get("batch")));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
