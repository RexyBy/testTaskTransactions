package by.rexy.testtask.transaction.controller;

import by.rexy.testtask.transaction.dto.TransactionDto;
import by.rexy.testtask.transaction.service.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionGateway transactionGateway;

    @PostMapping
    public TransactionDto createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionGateway.createTransaction(transactionDto);
    }

    @GetMapping("/{id}")
    public TransactionDto getTransaction(@PathVariable("id") int transactionId) {
        return transactionGateway.getTransaction(transactionId);
    }

    @PutMapping
    public TransactionDto updateTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionGateway.updateTransaction(transactionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable("id") int transactionId) {
        transactionGateway.deleteTransaction(transactionId);
    }

    @GetMapping("/search")
    public List<TransactionDto> searchTransactions(@RequestParam(value = "filters", required = false) String filters) {
        return transactionGateway.searchTransaction(filters);
    }
}
