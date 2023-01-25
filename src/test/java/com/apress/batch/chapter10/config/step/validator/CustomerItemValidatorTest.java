package com.apress.batch.chapter10.config.step.validator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@JdbcTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
class CustomerItemValidatorTest {

    @Autowired
    private DataSource dataSource;
    private CustomerItemValidator validator;

    @BeforeEach
    public void setUp() {
        this.validator = new CustomerItemValidator(this.dataSource);
    }

    @Test
    @DisplayName("Customer 객체의 정보가 customer table에 존재하는 경우")
    public void testCustomerExists() {
        //given
        CustomerUpdate customer = new CustomerUpdate(1L);

        //when & then
        validator.validate(customer);
    }

    @Test
    @DisplayName("Customer 객체의 정보가 customer table에 존재하지 않는 경우")
    public void testNoCustomer() {
        //given
        CustomerUpdate customer = new CustomerUpdate(-5L);

        //when & then
        assertThatThrownBy(() -> validator.validate(customer))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Customer id -5 was not able to be found");
    }

}