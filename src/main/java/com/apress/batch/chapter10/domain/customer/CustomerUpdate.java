package com.apress.batch.chapter10.domain.customer;

import lombok.Getter;

@Getter
public class CustomerUpdate {

    protected final long customerId;

    public CustomerUpdate(long customerId) {
        this.customerId = customerId;
    }
}
