package com.apress.batch.chapter10.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Transaction {
    private long transactionId;
    private long accountId;
    private String description;
    private BigDecimal credit;
    private BigDecimal debit;
    private LocalDateTime timestamp;

    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getTransactionAmount() {
        if (credit != null && debit != null) {
            return credit.add(debit);
        }

        if (credit != null && debit == null) {
            return credit;
        }

        return Objects.requireNonNullElseGet(debit, () -> new BigDecimal(0));

    }

}
