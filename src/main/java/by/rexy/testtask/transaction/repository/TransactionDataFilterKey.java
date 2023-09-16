package by.rexy.testtask.transaction.repository;

import lombok.Getter;

@Getter
public enum TransactionDataFilterKey {
    KEY("dataKey"),
    VALUE("dataValue");

    private final String fieldMapping;

    TransactionDataFilterKey(String fieldMapping) {
        this.fieldMapping = fieldMapping;
    }
}
