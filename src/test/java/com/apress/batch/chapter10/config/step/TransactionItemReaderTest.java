package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.config.ImportJobConfig;
import com.apress.batch.chapter10.config.step.listener.ApplyTransactionLogListener;
import com.apress.batch.chapter10.config.step.listener.GenerateStatementLogListener;
import com.apress.batch.chapter10.config.step.listener.ImportCustomerLogListener;
import com.apress.batch.chapter10.config.step.listener.ImportTransactionLogListener;
import com.apress.batch.chapter10.config.step.processor.AccountItemProcessor;
import com.apress.batch.chapter10.config.step.validator.CustomerItemValidator;
import com.apress.batch.chapter10.config.step.writer.CustomerUpdateItemWriter;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@TestPropertySource(properties = {"spring.cloud.task.closecontext_enable=false"})
@SpringBatchTest
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
        assertThat(tx.getTimestamp()).isNotNull();
    }

}
