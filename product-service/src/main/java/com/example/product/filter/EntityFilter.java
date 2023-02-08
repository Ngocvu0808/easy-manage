package com.example.product.filter;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class EntityFilter<T> {

  public static final Integer SEVEN_PREDICATE = 7;
  public static final Integer SIX_PREDICATE = 6;
  public static final Integer FIVE_PREDICATE = 5;
  public static final Integer FOUR_PREDICATE = 4;
  public static final Integer THREE_PREDICATE = 3;
  public static final Integer TWO_PREDICATE = 2;
  public static final Integer ONE_PREDICATE = 1;
  public static final Integer ZEZO_PREDICATE = 0;

  public EntityFilter() {
  }

  public Predicate statisticPredicate(Root<T> root, CriteriaBuilder criteriaBuilder,
      CriteriaQuery<?> criteriaQuery, int sizePredicate, List<Predicate> predicates) {
    criteriaQuery.distinct(true);
    if (sizePredicate == SEVEN_PREDICATE) {
      return criteriaBuilder.and(new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE),
          (Predicate) predicates.get(ONE_PREDICATE), (Predicate) predicates.get(TWO_PREDICATE),
          (Predicate) predicates.get(THREE_PREDICATE), (Predicate) predicates.get(FOUR_PREDICATE),
          (Predicate) predicates.get(FIVE_PREDICATE), (Predicate) predicates.get(SIX_PREDICATE)});
    } else if (sizePredicate == SIX_PREDICATE) {
      return criteriaBuilder.and(new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE),
          (Predicate) predicates.get(ONE_PREDICATE), (Predicate) predicates.get(TWO_PREDICATE),
          (Predicate) predicates.get(THREE_PREDICATE), (Predicate) predicates.get(FOUR_PREDICATE),
          (Predicate) predicates.get(FIVE_PREDICATE)});
    } else if (sizePredicate == FIVE_PREDICATE) {
      return criteriaBuilder.and(new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE),
          (Predicate) predicates.get(ONE_PREDICATE), (Predicate) predicates.get(TWO_PREDICATE),
          (Predicate) predicates.get(THREE_PREDICATE), (Predicate) predicates.get(FOUR_PREDICATE)});
    } else if (sizePredicate == FOUR_PREDICATE) {
      return criteriaBuilder.and(new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE),
          (Predicate) predicates.get(ONE_PREDICATE), (Predicate) predicates.get(TWO_PREDICATE),
          (Predicate) predicates.get(THREE_PREDICATE)});
    } else if (sizePredicate == THREE_PREDICATE) {
      return criteriaBuilder.and(new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE),
          (Predicate) predicates.get(ONE_PREDICATE), (Predicate) predicates.get(TWO_PREDICATE)});
    } else if (sizePredicate == TWO_PREDICATE) {
      return criteriaBuilder.and((Expression) predicates.get(ZEZO_PREDICATE),
          (Expression) predicates.get(ONE_PREDICATE));
    } else {
      return sizePredicate == ONE_PREDICATE ? criteriaBuilder.and(
          new Predicate[]{(Predicate) predicates.get(ZEZO_PREDICATE)}) : null;
    }
  }
}
