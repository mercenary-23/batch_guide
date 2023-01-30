package com.apress.batch.chapter10.domain.customer;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerUpdate {

    private final long customerId;

    public CustomerUpdate(long customerId) {
        this.customerId = customerId;
    }
}
