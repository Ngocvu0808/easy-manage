package com.example.authservice.filter;

import com.example.authservice.entities.application.RefreshToken;
import com.example.authservice.entities.enums.RefreshTokenStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RefreshTokenFilter extends EntityFilter<RefreshToken> {

  public Specification<RefreshToken> getByFilter(Integer apiKey, List<String> status,
      Map<String, String> sort, boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> client = root.join("client");
      List<Predicate> predicates = new ArrayList<>();
      List<RefreshTokenStatus> list = new ArrayList<>();
      status.forEach(s -> {
        try {
          list.add(RefreshTokenStatus.valueOf(s));
        } catch (Exception ignored) {

        }
      });
      if (list.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("status")).value(list));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "clientName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(client.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(client.get("name")));
              }
              break;
            case "clientId":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(client.get("clientId")));
              } else {
                orderList.add(criteriaBuilder.desc(client.get("clientId")));
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
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}