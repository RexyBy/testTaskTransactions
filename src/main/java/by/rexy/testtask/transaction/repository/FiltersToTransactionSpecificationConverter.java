package by.rexy.testtask.transaction.repository;

import by.rexy.testtask.transaction.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

public interface FiltersToTransactionSpecificationConverter {
    Specification<Transaction> convert(String filters);
}
