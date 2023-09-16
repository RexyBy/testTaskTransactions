package by.rexy.testtask.transaction.repository;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionFilterKey {

    ACTOR("actor"),
    TYPE("type"),
    TIMESTAMP_FROM("timestamp"),
    TIMESTAMP_TO("timestamp"),
    TRANSACTION_DATA("");

    private final String fieldMapping;

    TransactionFilterKey(String fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public static TransactionFilterKey getFilterKey(String filter) {
        return Arrays.stream(values())
                .filter(key -> key.name().equalsIgnoreCase(filter))
                .findFirst()
                .orElse(TRANSACTION_DATA);
    }

    public boolean isTimeRelated() {
        return TIMESTAMP_FROM == this || TIMESTAMP_TO == this;
    }
}
