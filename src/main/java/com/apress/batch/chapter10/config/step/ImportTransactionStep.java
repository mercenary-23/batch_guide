package com.apress.batch.chapter10.config.step;

import com.apress.batch.chapter10.config.step.listener.ExceptionLogListner;
import com.apress.batch.chapter10.domain.transaction.Transaction;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@RequiredArgsConstructor
public class ImportTransactionStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final ExceptionLogListner exceptionLogListner;

    @Bean
    public Step importTransactions() {
        return stepBuilderFactory.get("importTransactions")
            .<Transaction, Transaction>chunk(100)
            .reader(transactionItemReader(null))
            .writer(transactionItemWriter(null))
            .listener(exceptionLogListner)
            .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<Transaction> transactionItemReader(
        @Value("#{jobParameters['transactionFile']}") Resource transactionFile) {
        Jaxb2Marshaller unMarshaller = new Jaxb2Marshaller();
        unMarshaller.setClassesToBeBound(Transaction.class);

        return new StaxEventItemReaderBuilder<Transaction>()
            .name("fooReader")
            .resource(transactionFile)
            .addFragmentRootElements("transaction")
            .unmarshaller(unMarshaller)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> transactionItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
            .dataSource(dataSource)
            .sql("INSERT INTO transaction ("
                + "account_account_id, "
                + "description, "
                + "credit, "
                + "debit, "
                + "timestamp) values ("
                + ":accountId, "
                + ":description, "
                + ":credit, "
                + ":debit, "
                + ":timestamp)")
            .beanMapped()
            .build();
    }

}
