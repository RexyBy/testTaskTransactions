package by.rexy.testtask.transaction.service;

import by.rexy.testtask.transaction.converter.TransactionConverter;
import by.rexy.testtask.transaction.converter.TransactionDataConverter;
import by.rexy.testtask.transaction.dto.TransactionDto;
import by.rexy.testtask.transaction.model.Transaction;
import by.rexy.testtask.transaction.model.TransactionData;
import by.rexy.testtask.transaction.repository.FiltersToTransactionSpecificationConverter;
import by.rexy.testtask.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService implements TransactionGateway {
    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final TransactionDataConverter transactionDataConverter;
    private final FiltersToTransactionSpecificationConverter filtersToTransactionSpecificationConverter;

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        if (transactionRepository.existsById(transactionDto.id())) {
            String errorMessage = String.format("There is existing transaction with id=%s", transactionDto.id());
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Transaction transactionModel = transactionConverter.convert(transactionDto);
        Transaction savedTransaction = transactionRepository.save(transactionModel);

        return transactionConverter.convert(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        Transaction transactionToUpdate = transactionRepository.findById(transactionDto.id())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("There is no existing transaction with id=%s", transactionDto.id())));

        transactionToUpdate.setType(transactionDto.type());
        transactionToUpdate.setActor(transactionDto.actor());
        transactionToUpdate.setTimestamp(transactionDto.timestamp());

        List<TransactionData> updatedTransactionData = transactionDataConverter.convert(
                transactionDto.transactionData(),
                transactionToUpdate);
        List<TransactionData> currentTransactionData = transactionToUpdate.getTransactionData();
        removeOutdatedTransactionData(currentTransactionData, updatedTransactionData);
        updatedTransactionData.forEach(data -> updateTransactionData(currentTransactionData, data));

        Transaction savedTransaction = transactionRepository.save(transactionToUpdate);

        return transactionConverter.convert(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransaction(int transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("There is no existing transaction with id=%s", transactionId)));

        return transactionConverter.convert(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public List<TransactionDto> searchTransaction(String filters) {
        Specification<Transaction> specification = filtersToTransactionSpecificationConverter.convert(filters);
        List<Transaction> transactions = transactionRepository.findAll(specification);

        return transactions.stream()
                .map(transactionConverter::convert)
                .toList();
    }

    private void updateTransactionData(List<TransactionData> currentTransactionData, TransactionData transactionData) {
        Optional<TransactionData> existedData = currentTransactionData.stream()
                .filter(currentData -> currentData.getDataKey().equals(transactionData.getDataKey()))
                .findFirst();
        if (existedData.isPresent()) {
            existedData.get().setDataValue(transactionData.getDataValue());
        } else {
            currentTransactionData.add(transactionData);
        }
    }

    private void removeOutdatedTransactionData(List<TransactionData> currentTransactionData,
                                               List<TransactionData> updatedTransactionData) {
        currentTransactionData.removeIf(
                nextData -> updatedTransactionData.stream()
                        .noneMatch(
                                updatedData -> updatedData.getDataKey().equals(nextData.getDataKey())
                        ));
    }
}
