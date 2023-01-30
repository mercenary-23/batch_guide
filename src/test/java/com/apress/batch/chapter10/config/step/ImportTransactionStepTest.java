package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBatchTest
@SpringBootTest
@Sql("classpath:schema.sql")
public class ImportTransactionStepTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("ImportTransactionStep TEST")
    void testImportTransactionStep() {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("transactionFile", "classpath:transactionFile.xml")
            .toJobParameters();

        //when
        JobExecution jobExecution =
            this.jobLauncherTestUtils.launchStep("importTransactions",
                jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        List<Transaction> results = jdbcTemplate.query(
            "SELECT * FROM transaction WHERE transaction_id = 7788877",
            (rs, rowNum) -> {
                return Transaction.builder()
                    .transactionId(rs.getLong("transaction_id"))
                    .accountId(rs.getLong("account_account_id"))
                    .description(rs.getString("description"))
                    .credit(BigDecimal.valueOf(rs.getDouble("credit")))
                    .debit(BigDecimal.valueOf(rs.getDouble("debit")))
                    .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                    .build();
            });
        Transaction result = results.get(0);

        assertThat(result.getTransactionId()).isEqualTo(7788877L);
        assertThat(result.getAccountId()).isEqualTo(17L);
        assertThat(result.getDescription()).isEqualTo("Tom");
        assertThat(result.getCredit().intValue()).isEqualTo(538);
        assertThat(result.getDebit().intValue()).isEqualTo(-438);
        assertThat(result.getTimestamp().toString()).isEqualTo("2018-06-01T19:39:53");
    }
}
