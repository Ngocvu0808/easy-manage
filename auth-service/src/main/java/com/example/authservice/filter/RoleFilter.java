package com.example.authservice.filter;

import com.example.authservice.entities.role.Role;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoleFilter extends EntityFilter<Role> {

  public Specification<Role> getByFilter(String search, Map<String, String> sort,
      boolean isDeleted, Boolean isSystemRole) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      if (isSystemRole != null) {
        predicates.add(criteriaBuilder.equal(root.get("isSystemRole"), isSystemRole));
      }

      if (search != null && !search.isBlank()) {
        Predicate p1 = criteriaBuilder.like(root.get("code"), "%" + search.toLowerCase() + "%");
        Predicate p2 = criteriaBuilder.like(root.get("name"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(p1, p2));
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