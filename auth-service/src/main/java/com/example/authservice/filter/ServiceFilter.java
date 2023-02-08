package com.example.authservice.filter;

import com.example.authservice.entities.enums.ServiceStatus;
import com.example.authservice.entities.service.Service;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceFilter extends EntityFilter<Service> {

  public Specification<Service> getByFilter(String search, Set<Integer> listSystemId,
      Set<ServiceStatus> listStatus,
      Map<String, String> sort, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();
      Join<Object, Object> system = root.join("system", JoinType.LEFT);
      Join<Object, Object> serviceTags = root.join("serviceTags", JoinType.LEFT);
      Join<Object, Object> tag = serviceTags.join("tag", JoinType.LEFT);
      Join<Object, Object> creatorUser = root.join("creatorUser", JoinType.LEFT);

      if (search != null && !search.isBlank()) {
        Predicate nameService = criteriaBuilder
            .like(root.get("name"), "%" + search.toLowerCase() + "%");

        Predicate code = criteriaBuilder.like(root.get("code"), "%" + search.toLowerCase() + "%");

        Predicate nameSystem = criteriaBuilder
            .like(system.get("name"), "%" + search.toLowerCase() + "%");

        Predicate tagName = criteriaBuilder.like(tag.get("tag"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(nameService, code, nameSystem, tagName));
      }

      if (listStatus != null && !listStatus.isEmpty()) {
        predicates.add(criteriaBuilder.in(root.get("status")).value(listStatus));
      }
      if (listSystemId != null && !listSystemId.isEmpty()) {
        predicates.add(criteriaBuilder.in(system.get("id")).value(listSystemId));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "nameSystem":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(system.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(system.get("name")));
              }
              break;
            case "creatorName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(creatorUser.get("username")));
              } else {
                orderList.add(criteriaBuilder.desc(creatorUser.get("username")));
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


  public Specification<Service> getServiceFilter(String search, Set<Integer> listSystemId,
      Set<Integer> serviceIds, Map<String, String> sort, ServiceStatus status, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();
      Join<Object, Object> system = root.join("system", JoinType.LEFT);
      Join<Object, Object> serviceTags = root.join("serviceTags", JoinType.LEFT);
      Join<Object, Object> tag = serviceTags.join("tag", JoinType.LEFT);

      if (search != null && !search.isBlank()) {
        Predicate nameService = criteriaBuilder
            .like(root.get("name"), "%" + search.toLowerCase() + "%");

        Predicate code = criteriaBuilder.like(root.get("code"), "%" + search.toLowerCase() + "%");

        Predicate nameSystem = criteriaBuilder
            .like(system.get("name"), "%" + search.toLowerCase() + "%");

        Predicate tagName = criteriaBuilder.like(tag.get("tag"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(nameService, code, nameSystem, tagName));
      }

      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }

      if (listSystemId != null && !listSystemId.isEmpty()) {
        predicates.add(criteriaBuilder.in(system.get("id")).value(listSystemId));
      }

      if (serviceIds != null && !serviceIds.isEmpty()) {
        predicates.add(criteriaBuilder.not(root.get("id").in(serviceIds)));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "nameSystem":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(system.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(system.get("name")));
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
