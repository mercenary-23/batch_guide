package com.apress.batch.chapter10.config.step;

import com.apress.batch.chapter10.config.step.listener.ApplyTransactionLogListener;
import com.apress.batch.chapter10.config.step.listener.ImportCustomerLogListener;
import com.apress.batch.chapter10.domain.transaction.Transaction;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@RequiredArgsConstructor
public class ApplyTransactionStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplyTransactionLogListener applyTransactionLogListener;

    @Bean
    public Step applyTransactions() {
        return stepBuilderFactory.get("applyTransactions")
            .<Transaction, Transaction>chunk(100)
            .reader(applyTransactionReader(null, null))
            .writer(applyTransactionWriter(null))
            .listener(applyTransactionLogListener)
            .build();
    }

    @Bean
    public JdbcPagingItemReader<Transaction> applyTransactionReader(DataSource dataSource, PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<Transaction>()
            .name("applyTransactionReader")
            .dataSource(dataSource)
            .queryProvider(queryProvider)
            .pageSize(100)
            .rowMapper(transactionRowMapper())
            .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider(DataSource dataSource) throws Exception {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();

        factoryBean.setSelectClause("SELECT *");
        factoryBean.setFromClause("FROM transaction");
        factoryBean.setSortKey("timestamp");
        factoryBean.setDataSource(dataSource);

        return factoryBean.getObject();
    }

    private RowMapper<Transaction> transactionRowMapper()  {
        return (rs, rowNum) -> Transaction
            .builder()
            .transactionId(rs.getLong("transaction_id"))
            .accountId(rs.getLong("account_account_id"))
            .description(rs.getString("description"))
            .credit(rs.getBigDecimal("credit"))
            .debit(rs.getBigDecimal("debit"))
            .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
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
