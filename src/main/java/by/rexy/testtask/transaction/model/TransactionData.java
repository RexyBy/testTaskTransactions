package by.rexy.testtask.transaction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class TransactionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String dataKey;

    private String dataValue;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public TransactionData(String dataKey, String dataValue, Transaction transaction) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.transaction = transaction;
    }
}
