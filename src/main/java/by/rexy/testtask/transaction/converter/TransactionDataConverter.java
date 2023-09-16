package by.rexy.testtask.transaction.converter;

import by.rexy.testtask.transaction.model.Transaction;
import by.rexy.testtask.transaction.model.TransactionData;

import java.util.List;
import java.util.Map;

public interface TransactionDataConverter {
    Map<String, String> convert(List<TransactionData> transactionData);

    List<TransactionData> convert(Map<String, String> map, Transaction transaction);
}
