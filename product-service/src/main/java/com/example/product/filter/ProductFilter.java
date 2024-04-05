package com.example.product.filter;

import com.example.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductFilter extends EntityFilter<Product> {
  public Specification<Product> filter(Set<Integer> ids, String search, String status,
      Map<String, String> sort,  String promotionCode) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate batch = criteriaBuilder.like(root.get("name"), searchValue.trim());
        Predicate code = criteriaBuilder.like(root.get("code"), searchValue.trim());
        Predicate type = criteriaBuilder.like(root.get("type"), searchValue.trim());
        predicates.add(criteriaBuilder.or(batch, code, type));
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      if (ids != null && ids.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
      }
      if (promotionCode != null  && !StringUtils.isEmpty(promotionCode)) {
        predicates.add(criteriaBuilder.equal(root.get("promotion"), promotionCode));
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


  public Specification<Product> filterDiscount(String promotion) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(criteriaBuilder.equal(root.get("promotion"), promotion));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }

}
