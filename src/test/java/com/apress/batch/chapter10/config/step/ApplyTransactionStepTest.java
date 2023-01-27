package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.domain.statement.Account;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBatchTest
@SpringBootTest
@Sql("classpath:schema.sql")
@Sql("classpath:transaction.sql")
public class ApplyTransactionStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("ApplyTransactionStep TEST")
    void testApplyTransactionStep() {
        //when
        JobExecution jobExecution =
            this.jobLauncherTestUtils.launchStep("applyTransactions");

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        List<Account> results = jdbcTemplate.query("SELECT * FROM account WHERE account_id = 1",
            (rs, rowNum) -> {
                return new Account(rs.getLong("account_id"), rs.getBigDecimal("balance"),
                    rs.getTimestamp("last_statement_date").toLocalDateTime());
            });
        Account result = results.get(0);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBalance().intValue()).isEqualTo(1000);
    }
}
