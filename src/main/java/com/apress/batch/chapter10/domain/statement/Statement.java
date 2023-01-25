package com.apress.batch.chapter10.domain.statement;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Statement {

    private final Customer customer;
    private List<Account> accounts = new ArrayList<>();

    public Statement(Customer customer, List<Account> accounts) {
        this.customer = customer;
        this.accounts.addAll(accounts);
    }

    public Statement(Customer customer) {
        this.customer = customer;
    }
}
