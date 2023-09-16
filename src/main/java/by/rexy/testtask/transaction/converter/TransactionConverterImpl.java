package by.rexy.testtask.transaction.converter;

import by.rexy.testtask.transaction.dto.TransactionDto;
import by.rexy.testtask.transaction.model.Transaction;
import by.rexy.testtask.transaction.model.TransactionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TransactionConverterImpl implements TransactionConverter {
    private final TransactionDataConverter transactionDataConverter;

    @Override
    public TransactionDto convert(Transaction transaction) {
        Map<String, String> transactionData = transactionDataConverter.convert(transaction.getTransactionData());

        return new TransactionDto(transaction.getId(),
                                  transaction.getTimestamp(),
                                  transaction.getActor(),
                                  transaction.getType(),
                                  transactionData);
    }

    @Override
    public Transaction convert(TransactionDto dto) {
        Transaction transaction = new Transaction(dto.id(),
                                                  dto.timestamp(),
                                                  dto.actor(),
                                                  dto.type());
        List<TransactionData> transactionData = transactionDataConverter.convert(dto.transactionData(), transaction);
        transaction.setTransactionData(transactionData);

        return transaction;
    }
}
