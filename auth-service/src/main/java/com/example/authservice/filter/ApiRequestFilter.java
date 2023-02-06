package com.example.authservice.filter;


import com.example.authservice.entities.application.ApiRequest;
import com.example.authservice.entities.enums.ApiRequestStatus;
import com.example.authservice.entities.enums.ApiType;
import com.example.authservice.entities.enums.ServiceStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiRequestFilter extends EntityFilter<ApiRequest> {

  public Specification<ApiRequest> filter(Set<Integer> systemIds, Set<Integer> serviceIds,
      Set<Integer> clientIds,
      Set<String> status, Set<String> types, String search, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> api = root.join("api", JoinType.LEFT);
      Join<Object, Object> client = root.join("client", JoinType.LEFT);
      Join<Object, Object> service = api.join("service", JoinType.LEFT);
      Join<Object, Object> system = service.join("system", JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(service.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(service.get("status"), ServiceStatus.ACTIVE));
      if (status.size() > 0) {
        List<ApiRequestStatus> statusList = new ArrayList<>();
        for (String s : status) {
          try {
            statusList.add(ApiRequestStatus.valueOf(s));
          } catch (Exception ignored) {

          }
        }
        if (!statusList.isEmpty()) {
          predicates.add(criteriaBuilder.in(root.get("status")).value(statusList));
        }
      }
      if (types.size() > 0) {
        List<ApiType> typeList = new ArrayList<>();
        for (String s : types) {
          try {
            typeList.add(ApiType.valueOf(s));
          } catch (Exception ignored) {

          }
        }
        if (!typeList.isEmpty()) {
          predicates.add(criteriaBuilder.in(api.get("type")).value(typeList));
        }
      }
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(api.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(api.get("api"), searchValue);
//        Predicate pr3 = criteriaBuilder.like(api.get("type"), searchValue);
        Predicate pr4 = criteriaBuilder
            .and(criteriaBuilder.equal(service.get("isDeleted"), false),
                criteriaBuilder.like(service.get("name"), searchValue));
        Predicate pr5 = criteriaBuilder
            .and(criteriaBuilder.equal(service.get("isDeleted"), false),
                criteriaBuilder.equal(system.get("isDeleted"), false),
                criteriaBuilder.like(system.get("name"), searchValue));
        predicates.add(criteriaBuilder.or(pr1, pr2, pr4, pr5));
      }
      if (clientIds != null && !clientIds.isEmpty()) {
        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(client.get("isDeleted"), false),
            criteriaBuilder.in(client.get("id")).value(clientIds)));
      }
      if (systemIds != null && !systemIds.isEmpty()) {
        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(service.get("isDeleted"), false),
            criteriaBuilder.equal(system.get("isDeleted"), false),
            criteriaBuilder.in(system.get("id")).value(systemIds)));
        if (serviceIds != null && !serviceIds.isEmpty()) {
          predicates.add(criteriaBuilder.and(criteriaBuilder.equal(service.get("isDeleted"), false),
              criteriaBuilder.in(system.get("id")).value(systemIds),
              criteriaBuilder.in(service.get("id")).value(serviceIds)));
        }
      } else {
        if (serviceIds != null && !serviceIds.isEmpty()) {
          predicates.add(criteriaBuilder.and(criteriaBuilder.equal(service.get("isDeleted"), false),
              criteriaBuilder.in(service.get("id")).value(serviceIds)));
        }
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "system":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(system.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(system.get("name")));
              }
              break;
            case "service":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(service.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(service.get("name")));
              }
              break;
            case "client":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(client.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(client.get("name")));
              }
              break;
            default:
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(api.get(key)));
              } else {
                orderList.add(criteriaBuilder.desc(api.get(key)));
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