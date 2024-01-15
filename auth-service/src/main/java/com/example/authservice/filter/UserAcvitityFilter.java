package com.example.authservice.filter;

import com.example.authservice.entities.UserActivity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserAcvitityFilter extends EntityFilter<UserActivity>{

  public Specification<UserActivity> filter(Set<Integer> ids, String search, String status,
      Map<String, String> sort, Date startDate, Date endDate) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        if (ids != null && ids.size() > 0) {
        Predicate batch = criteriaBuilder.in(root.get("userId")).value(ids);
        Predicate code = criteriaBuilder.like(root.get("sessionId"), searchValue.trim());
        Predicate type = criteriaBuilder.like(root.get("activity"), searchValue.trim());
        Predicate ip = criteriaBuilder.like(root.get("IPAddress"), searchValue.trim());
          predicates.add(criteriaBuilder.or(batch, code, type, ip));
        } else {
          Predicate code = criteriaBuilder.like(root.get("sessionId"), searchValue.trim());
          Predicate type = criteriaBuilder.like(root.get("activity"), searchValue.trim());
          Predicate ip = criteriaBuilder.like(root.get("IPAddress"), searchValue.trim());
          predicates.add(criteriaBuilder.or(code, type, ip));
        }
      }

      if (startDate != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createTime"), startDate));
      }
      if (endDate != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createTime"), endDate));
      }
      criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }

}
