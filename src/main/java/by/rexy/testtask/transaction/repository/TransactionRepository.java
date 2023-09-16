package by.rexy.testtask.transaction.repository;

import by.rexy.testtask.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>,
        JpaSpecificationExecutor<Transaction> {
}
