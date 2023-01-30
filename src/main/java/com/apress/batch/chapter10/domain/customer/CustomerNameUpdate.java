package com.apress.batch.chapter10.domain.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class CustomerNameUpdate extends CustomerUpdate{

    private final String firstName;
    private final String middleName;
    private final String lastName;

    @Builder
    public CustomerNameUpdate(long customerId, String firstName, String middleName,
        String lastName) {
        super(customerId);
        this.firstName = StringUtils.hasText(firstName) ? firstName : null;
        this.middleName = StringUtils.hasText(middleName) ? middleName : null;
        this.lastName = StringUtils.hasText(lastName) ? lastName : null;
    }
}
