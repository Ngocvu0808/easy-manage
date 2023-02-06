package com.example.authservice.filter;


import com.example.authservice.entities.application.ClientApiKey;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ClientApiKeyFilter extends EntityFilter<ClientApiKey> {

  public Specification<ClientApiKey> filter(Integer appId, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (appId != null) {
        predicates.add(criteriaBuilder.equal(root.get("clientId"), appId));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            default:
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(root.get(key)));
              } else {
                orderList.add(criteriaBuilder.desc(root.get(key)));
              }
              break;
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
