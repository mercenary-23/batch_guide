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
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImportJobConfig.class,
    ImportCustomerStep.class, ImportTransactionStep.class,
    ApplyTransactionStep.class, GenerateStatementStep.class,
    CustomerItemValidator.class, AccountItemProcessor.class})
@EnableBatchProcessing
@SpringBatchTest
@JdbcTest
public class TransactionItemReaderTest {

    @Autowired
    private StaxEventItemReader<Transaction> staxEventItemReader;

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("transactionFile", "classpath:transactionFile.xml")
            .toJobParameters();

        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    @DisplayName("TransactionItemReader TEST")
    void testTransactionItemReader() throws Exception {
        //given
        staxEventItemReader.open(new ExecutionContext());

        //when & then
        Transaction tx = staxEventItemReader.read();
        assertThat(tx.getTransactionId()).isEqualTo(7788877L);
        assertThat(tx.getAccountId()).isEqualTo(17L);
        assertThat(tx.getDescription()).isEqualTo("Tom");
        assertThat(tx.getCredit().intValue()).isEqualTo(538);
        assertThat(tx.getDebit().intValue()).isEqualTo(-438);
    }

}
