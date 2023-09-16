package by.rexy.testtask.transaction.converter;

import by.rexy.testtask.transaction.model.Transaction;
import by.rexy.testtask.transaction.model.TransactionData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionDataConverterImpl implements TransactionDataConverter {
    @Override
    public Map<String, String> convert(List<TransactionData> transactionData) {
        return transactionData.stream()
                .collect(Collectors.toMap(
                        TransactionData::getDataKey,
                        TransactionData::getDataValue
                ));
    }

    @Override
    public List<TransactionData> convert(Map<String, String> map, Transaction transaction) {
        return map.entrySet().stream()
                .map(entry -> new TransactionData(entry.getKey(), entry.getValue(), transaction))
                .toList();
    }
}
