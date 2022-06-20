package de.earlgreyt.date4u.repositories.search;

import de.earlgreyt.date4u.core.entitybeans.Profile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProfileSpec implements Specification<Profile> {
  private List<SearchCriteria> criteriaList = new LinkedList<>();
  public void addCriteria(SearchCriteria criteria){
    criteriaList.add(criteria);
  }
  @Override
  public Predicate toPredicate(Root<Profile> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicateList = new ArrayList<>();
    for (SearchCriteria criteria : criteriaList) {
      switch (criteria.getOperation()){
        case EQUAL ->  predicateList.add(criteriaBuilder.equal(root.get(criteria.getKey()),criteria.getValue().toString()));
        case NOT_EQUAL -> predicateList.add(criteriaBuilder.notEqual(root.get(criteria.getKey()),criteria.getValue().toString()));
        case GREATER_THAN -> predicateList.add(criteriaBuilder.greaterThan(root.get(criteria.getKey()),criteria.getValue().toString()));
        case GREATER_THAN_EQUAL -> predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()),criteria.getValue().toString()));
        case LESS_THAN -> predicateList.add(criteriaBuilder.lessThan(root.get(criteria.getKey()),criteria.getValue().toString()));
        case LESS_THAN_EQUAL -> predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()),criteria.getValue().toString()));
        case IN -> predicateList.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));
        case NOT_IN -> predicateList.add(criteriaBuilder.not(root.get(criteria.getKey())).in(criteria.getValue()));
        case MATCH -> predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
            "%" + criteria.getValue().toString().toLowerCase() + "%"));
        case MATCH_END -> predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
            criteria.getValue().toString().toLowerCase() + "%"));
        case MATCH_START -> predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
            "%" + criteria.getValue().toString().toLowerCase()));
      }
    }
    return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
  }
}
