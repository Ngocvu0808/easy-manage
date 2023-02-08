package com.example.authservice.filter;

import com.example.authservice.entities.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupFilter extends EntityFilter<Group> {

  public Specification<Group> getByFilter(String search, List<Integer> listRole,
      Map<String, String> sort, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();
      Join<Object, Object> roleGroups = root.join("roles", JoinType.LEFT);
      Join<Object, Object> role = roleGroups.join("role", JoinType.LEFT);

      if (search != null && !search.isBlank()) {
        Predicate nameGroup = criteriaBuilder
            .like(root.get("name"), "%" + search.toLowerCase() + "%");
        Predicate code = criteriaBuilder.like(root.get("code"), "%" + search.toLowerCase() + "%");

        Predicate nameRole = criteriaBuilder
            .like(role.get("name"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(nameGroup, code, nameRole));
      }
      if (listRole != null && !listRole.isEmpty()) {
        predicates.add(criteriaBuilder.equal(roleGroups.get("isDeleted"), false));
        predicates.add(criteriaBuilder.in(role.get("id")).value(listRole));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "roles":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(role.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(role.get("name")));
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
