package com.example.authservice.filter;

import com.example.authservice.entities.UserStatus;
import com.example.authservice.entities.user.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserFilter extends EntityFilter<User> {

  public Specification<User> filter(List<String> status, List<Integer> roles,
      List<Integer> groups, String search, Map<String, String> sort, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> userRoles = root.join("roles", JoinType.LEFT);
      Join<Object, Object> role = userRoles.join("role", JoinType.LEFT);

      Join<Object, Object> userGroups = root.join("groups", JoinType.LEFT);
      Join<Object, Object> userGroup = userGroups.join("group", JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();
      if (status.size() > 0) {
        List<UserStatus> statusList = new ArrayList<>();
        for (String s : status) {
          try {
            statusList.add(UserStatus.valueOf(s));
          } catch (Exception ignored) {

          }
        }
        if (!statusList.isEmpty()) {
          predicates.add(criteriaBuilder.in(root.get("status")).value(statusList));
        }
      }
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("username"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("email"), searchValue);
        Predicate pr4 = criteriaBuilder
            .and(criteriaBuilder.equal(userRoles.get("isDeleted"), false),
                criteriaBuilder.like(role.get("name"), searchValue));
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
//            case "roles":
//              if (orderType.equals("asc")) {
//                orderList.add(criteriaBuilder.asc(role.get("name")));
//              } else {
//                orderList.add(criteriaBuilder.desc(role.get("name")));
//              }
//              break;
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

      if (roles != null && !roles.isEmpty()) {
        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(userRoles.get("isDeleted"), false),
            criteriaBuilder.equal(role.get("isDeleted"), false),
            criteriaBuilder.in(role.get("id")).value(roles)));
      }

      if (groups != null && !groups.isEmpty()) {
        predicates
            .add(criteriaBuilder.and(criteriaBuilder.equal(userGroups.get("isDeleted"), false),
                criteriaBuilder.equal(userGroup.get("isDeleted"), false),
                criteriaBuilder.in(userGroup.get("id")).value(groups)));
      }

      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      predicates.add(criteriaBuilder.isNull(root.get("isUserInternal")));
      // sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if (orderType.equals("asc")) {
            orderList.add(criteriaBuilder.asc(root.get(key)));
          } else {
            orderList.add(criteriaBuilder.desc(root.get(key)));
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }

  public Specification<User> getUserDetailFilter(Integer userId, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> userRoles = root.join("roles", JoinType.LEFT);
      Join<Object, Object> role = userRoles.join("role", JoinType.LEFT);

      Join<Object, Object> userGroups = root.join("groups", JoinType.LEFT);
      Join<Object, Object> userGroup = userGroups.join("group", JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.and(criteriaBuilder.equal(userRoles.get("isDeleted"), false)));
      predicates
          .add(criteriaBuilder.and(criteriaBuilder.equal(userGroups.get("isDeleted"), false)));

      predicates.add(criteriaBuilder.equal(root.get("id"), userId));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }

  public Specification<User> getCustomerFilter(String search) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("username"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("email"), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }
      predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isUserInternal"), false)));
      predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isDeleted"), false)));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}