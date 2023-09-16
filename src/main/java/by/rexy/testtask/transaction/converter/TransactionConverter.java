package by.rexy.testtask.transaction.converter;

import by.rexy.testtask.transaction.dto.TransactionDto;
import by.rexy.testtask.transaction.model.Transaction;

public interface TransactionConverter {
    TransactionDto convert(Transaction transaction);

    Transaction convert(TransactionDto dto);
}
