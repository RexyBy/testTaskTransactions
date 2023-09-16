package by.rexy.testtask.transaction.dto;

import java.time.Instant;
import java.util.Map;


public record TransactionDto(Integer id,
                             Instant timestamp,
                             String actor,
                             String type,
                             Map<String, String> transactionData) {
}