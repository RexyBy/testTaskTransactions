package by.rexy.testtask.transaction.service;


import by.rexy.testtask.transaction.dto.TransactionDto;

import java.util.List;

public interface TransactionGateway {
    TransactionDto createTransaction(TransactionDto transactionDto);

    TransactionDto updateTransaction(TransactionDto transactionDto);

    TransactionDto getTransaction(int transactionId);

    void deleteTransaction(int transactionId);

    List<TransactionDto> searchTransaction(String filters);
}
