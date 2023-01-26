package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.config.ImportJobConfig;
import com.apress.batch.chapter10.config.step.processor.AccountItemProcessor;
import com.apress.batch.chapter10.config.step.validator.CustomerItemValidator;
import com.apress.batch.chapter10.domain.transaction.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImportJobConfig.class,
    ImportCustomerStep.class, ImportTransactionStep.class,
    ApplyTransactionStep.class, GenerateStatementStep.class,
    CustomerItemValidator.class, AccountItemProcessor.class})
@EnableBatchProcessing
@SpringBatchTest
@JdbcTest
@Sql("classpath:schema.sql")
@Sql("classpath:data.sql")
public class ApplyTransactionReaderTest {

    @Autowired
    private JdbcCursorItemReader<Transaction> itemReader;

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
            .toJobParameters();
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    @DisplayName("ApplyTransactionReader TEST")
    void testApplyTransactionReader() throws Exception {
        //given
        itemReader.open(new ExecutionContext());

        //when & then
//        assertThat(itemReader.read()).isInstanceOf(Transaction.class);
    }
}
