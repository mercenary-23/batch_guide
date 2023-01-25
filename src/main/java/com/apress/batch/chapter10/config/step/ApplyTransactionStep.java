package com.apress.batch.chapter10.config.step;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplyTransactionStep {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step applyTransactions() {
        return stepBuilderFactory.get("applyTransactions")
            .<Transaction, Transaction>chunk(100)
            .reader(applyTransactionReader(null))
            .writer(applyTransactionWriter(null))
            .build();
    }

    @Bean
    public JdbcCursorItemReader<Transaction> applyTransactionReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
            .name("applyTransactionReader")
            .dataSource(dataSource)
            .sql("SELECT transaction_id, "
                + "account_account_id, "
                + "description, "
                + "credit, "
                + "debit, "
                + "timestamp "
                + "FROM transaction "
                + "ORDER BY timestamp")
            .rowMapper((rs, rowNum) -> Transaction
                .builder()
                .transactionId(rs.getLong("transaction_id"))
                .accountId(rs.getLong("account_account_id"))
                .description(rs.getString("description"))
                .credit(rs.getBigDecimal("credit"))
                .debit(rs.getBigDecimal("debit"))
                .timestamp(rs.getTimestamp("timestamp"))
                .build())
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> applyTransactionWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
            .dataSource(dataSource)
            .sql("UPDATE account SET "
                + "balance = balance + :transactionAmount "
                + "WHERE account_id = :accountId")
            .beanMapped()
            .assertUpdates(false)
            .build();
    }
}
