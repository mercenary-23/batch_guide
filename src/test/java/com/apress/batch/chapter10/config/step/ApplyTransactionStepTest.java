package com.apress.batch.chapter10.config.step;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);


    }
}
