package com.apress.batch.chapter10.domain.statement;

import com.apress.batch.chapter10.domain.customer.Customer;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Statement {

    private final Customer customer;
    private List<Account> accounts;

    public Statement(Customer customer, List<Account> accounts) {
        this.customer = customer;
        this.accounts = accounts;
    }

    public Statement(Customer customer) {
        this.customer = customer;
    }
}
