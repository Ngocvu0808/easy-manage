package com.example.authservice.filter;


import com.example.authservice.entities.application.Client;
import com.example.authservice.entities.enums.ClientStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientDetailFilter extends EntityFilter<Client> {

  public Specification<Client> filter(Set<Integer> ids, String search, ClientStatus status,
      Map<String, String> sort, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        predicates.add(criteriaBuilder.like(root.get("name"), searchValue));
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      if (ids != null && ids.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
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