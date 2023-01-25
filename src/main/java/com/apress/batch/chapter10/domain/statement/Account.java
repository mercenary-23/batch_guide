package com.apress.batch.chapter10.domain.statement;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

    private final long id;
    private final BigDecimal balance;
    private final Date lastStatementDate;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(long id, BigDecimal balance, Date lastStatementDate) {
        this.id = id;
        this.balance = balance;
        this.lastStatementDate = lastStatementDate;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
