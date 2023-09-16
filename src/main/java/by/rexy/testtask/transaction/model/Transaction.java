package by.rexy.testtask.transaction.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Transaction {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private Instant timestamp;

    private String actor;

    private String type;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionData> transactionData;

    public Transaction(Integer id, Instant timestamp, String actor, String type) {
        this.id = id;
        this.timestamp = timestamp;
        this.actor = actor;
        this.type = type;
    }
}
