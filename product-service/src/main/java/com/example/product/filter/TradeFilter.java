package com.example.product.filter;

import com.example.product.entity.Product;
import com.example.product.entity.TradeHistory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TradeFilter extends EntityFilter<TradeHistory> {
  public Specification<TradeHistory> filter(Set<Integer> ids, String search, String status,
      Map<String, String> sort, long startDate, long endDate) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate batch = criteriaBuilder.like(root.get("batch"), searchValue.trim());
        Predicate code = criteriaBuilder.like(root.get("productCode"), searchValue.trim());
        Predicate type = criteriaBuilder.like(root.get("type"), searchValue.trim());
        Predicate cusName = criteriaBuilder.like(root.get("cus_name"), searchValue.trim());
        Predicate username = criteriaBuilder.like(root.get("username"), searchValue.trim());
        predicates.add(criteriaBuilder.or(batch, code, type, cusName, username));
      }
      if (ids != null && ids.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
      }
      if (startDate != 0) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Long>get("createDate"), startDate));
      }
      if (endDate != 0) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Long>get("createDate"), endDate+86400000));
      }
      criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
