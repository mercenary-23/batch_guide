package com.apress.batch.chapter10.domain.statement;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {

    private final long id;
    private final BigDecimal balance;
    private final LocalDateTime lastStatementDate;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(long id, BigDecimal balance, LocalDateTime lastStatementDate) {
        this.id = id;
        this.balance = balance;
        this.lastStatementDate = lastStatementDate;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
