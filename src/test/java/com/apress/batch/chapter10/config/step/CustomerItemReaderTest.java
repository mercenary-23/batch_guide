package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.config.ImportJobConfig;
import com.apress.batch.chapter10.config.step.processor.AccountItemProcessor;
import com.apress.batch.chapter10.config.step.validator.CustomerItemValidator;
import com.apress.batch.chapter10.domain.customer.CustomerAddressUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerContactUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerNameUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
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
public class CustomerItemReaderTest {

    @Autowired
    private FlatFileItemReader<CustomerUpdate> customerUpdateItemReader;

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("customerUpdateFile", "classpath:customerUpdateFile.csv")
            .toJobParameters();

        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    @DisplayName("CustomerItemUpdateReader TEST")
    public void testCustomerItemReader() throws Exception {
        //given
        customerUpdateItemReader.open(new ExecutionContext());

        //when & then
        assertThat(customerUpdateItemReader.read()).isInstanceOf(
            CustomerAddressUpdate.class);
        assertThat(customerUpdateItemReader.read()).isInstanceOf(CustomerContactUpdate.class);
        assertThat(customerUpdateItemReader.read()).isInstanceOf(CustomerNameUpdate.class);
    }
}
