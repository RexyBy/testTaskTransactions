package by.rexy.testtask.transaction.repository;

import by.rexy.testtask.search.SearchCriterion;
import by.rexy.testtask.search.converter.FiltersStringToCriteriaConverter;
import by.rexy.testtask.transaction.model.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FiltersToTransactionSpecificationConverterImpl implements
        FiltersToTransactionSpecificationConverter {
    private static final String TRANSACTION_DATA_ATTRIBUTE = "transactionData";

    private final FiltersStringToCriteriaConverter filtersStringToCriteriaConverter;

    @Override
    public Specification<Transaction> convert(String filters) {
        List<SearchCriterion> searchCriteria = filtersStringToCriteriaConverter.convert(filters);

        return (root, query, criteriaBuilder) -> {
            root.join(TRANSACTION_DATA_ATTRIBUTE);
            Map<TransactionFilterKey, List<SearchCriterion>> groupedSearchCriteria =
                    new EnumMap<>(TransactionFilterKey.class);

            searchCriteria.forEach(criterion -> {
                TransactionFilterKey filterKey = TransactionFilterKey.getFilterKey(criterion.key());
                groupedSearchCriteria.computeIfAbsent(filterKey, n -> new ArrayList<>());
                groupedSearchCriteria.get(filterKey).add(criterion);
            });

            List<Predicate> predicates = toPredicates(groupedSearchCriteria, root, criteriaBuilder);

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
    }

    private List<Predicate> toPredicates(Map<TransactionFilterKey, List<SearchCriterion>> groupedSearchCriteria,
                                         Root<Transaction> root,
                                         CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<TransactionFilterKey, List<SearchCriterion>> entry : groupedSearchCriteria.entrySet()) {
            List<SearchCriterion> searchCriteria = entry.getValue();
            if (entry.getKey().isTimeRelated()) {
                if (entry.getValue().size() == 1) {
                    predicates.add(searchCriterionToPredicate(searchCriteria.get(0), root, criteriaBuilder));
                } else {
                    throw new IllegalArgumentException("There can't be more than one filter for time related filters");
                }
            } else {
                Predicate predicate = null;
                for (SearchCriterion searchCriterion : searchCriteria) {
                    if (predicate == null) {
                        predicate = searchCriterionToPredicate(searchCriterion, root, criteriaBuilder);
                    } else {
                        predicate = criteriaBuilder.or(predicate,
                                                       searchCriterionToPredicate(searchCriterion, root,
                                                                                  criteriaBuilder));
                    }
                }
                predicates.add(predicate);
            }
        }

        return predicates;
    }

    private Predicate searchCriterionToPredicate(SearchCriterion searchCriterion,
                                                 Root<Transaction> root,
                                                 CriteriaBuilder criteriaBuilder) {
        TransactionFilterKey filterKey = TransactionFilterKey.getFilterKey(searchCriterion.key());
        return switch (filterKey) {
            case TIMESTAMP_FROM -> retrieveTimestampFromPredicate(searchCriterion, root, criteriaBuilder, filterKey);
            case TIMESTAMP_TO -> retrieveTimestampToPredicate(searchCriterion, root, criteriaBuilder, filterKey);
            case ACTOR, TYPE -> criteriaBuilder.equal(root.get(filterKey.getFieldMapping()), searchCriterion.value());
            case TRANSACTION_DATA -> retrieveTransactionDataPredicate(searchCriterion, root, criteriaBuilder);
        };
    }

    private Predicate retrieveTimestampFromPredicate(SearchCriterion searchCriterion,
                                                     Root<Transaction> root,
                                                     CriteriaBuilder criteriaBuilder,
                                                     TransactionFilterKey filterKey) {
        Instant instantFrom = Instant.ofEpochSecond(Long.parseLong(searchCriterion.value()));
        return criteriaBuilder.greaterThanOrEqualTo(
                root.get(filterKey.getFieldMapping()), instantFrom);
    }

    private Predicate retrieveTimestampToPredicate(SearchCriterion searchCriterion,
                                                   Root<Transaction> root,
                                                   CriteriaBuilder criteriaBuilder,
                                                   TransactionFilterKey filterKey) {
        Instant instantTo = Instant.ofEpochSecond(Long.parseLong(searchCriterion.value()));
        return criteriaBuilder.lessThanOrEqualTo(
                root.get(filterKey.getFieldMapping()), instantTo);
    }

    private Predicate retrieveTransactionDataPredicate(SearchCriterion searchCriterion,
                                                       Root<Transaction> root,
                                                       CriteriaBuilder criteriaBuilder) {
        String keyColumn = TransactionDataFilterKey.KEY.getFieldMapping();
        String valueColumn = TransactionDataFilterKey.VALUE.getFieldMapping();
        return criteriaBuilder.and(
                criteriaBuilder.equal(root.get(TRANSACTION_DATA_ATTRIBUTE).get(keyColumn), searchCriterion.key()),
                criteriaBuilder.equal(root.get(TRANSACTION_DATA_ATTRIBUTE).get(valueColumn), searchCriterion.value())
        );
    }
}
