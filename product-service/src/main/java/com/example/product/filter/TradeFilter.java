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
        predicates.add(criteriaBuilder.or(batch, code, type));
      }
      if (ids != null && ids.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
      }
      if (startDate != 0) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Long>get("createDate"), startDate));
      }
      if (endDate != 0) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Long>get("createDate"), endDate));
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "creatorName":
              Join<Object, Object> creatorUser = root.join("creatorUser");
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(creatorUser.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(creatorUser.get("name")));
              }
              break;
            default:
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(root.get(key)));
              } else {
                orderList.add(criteriaBuilder.desc(root.get(key)));
              }
          }
        }
        criteriaQuery.orderBy(orderList);
      }

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
