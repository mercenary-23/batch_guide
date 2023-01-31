package com.apress.batch.chapter10.config;

import com.apress.batch.chapter10.config.step.ApplyTransactionStep;
import com.apress.batch.chapter10.config.step.GenerateStatementStep;
import com.apress.batch.chapter10.config.step.ImportCustomerStep;
import com.apress.batch.chapter10.config.step.ImportTransactionStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@EnableTask
public class ImportJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final ImportCustomerStep importCustomerStep;
    private final ImportTransactionStep importTransactionStep;
    private final ApplyTransactionStep applyTransactionStep;
    private final GenerateStatementStep generateStatementStep;

    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory.get("importJob")
            .incrementer(new RunIdIncrementer())
            .start(importCustomerStep.importCustomerUpdates())
            .next(importTransactionStep.importTransactions())
            .next(applyTransactionStep.applyTransactions())
            .next(generateStatementStep.generateStatements(null))
            .build();
    }
}
