package com.apress.batch.chapter10.config.step.validator;

import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemValidator implements Validator<CustomerUpdate> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerItemValidator(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void validate(CustomerUpdate customer) throws ValidationException {
        Long count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM customer WHERE customer_id = :id",
            new MapSqlParameterSource().addValue("id", customer.getCustomerId()), Long.class);

        Objects.requireNonNull(count,
            "JdbcTemplate의 queryForObject 메서드가 Null을 반환했습니다. SQL : SELECT COUNT(*) FROM customer WHERE customer_id = :id");

        if (count == 0) {
            throw new ValidationException(
                String.format("Customer id %s was not able to be found in the database",
                    customer.getCustomerId())
            );
        }
    }
}
