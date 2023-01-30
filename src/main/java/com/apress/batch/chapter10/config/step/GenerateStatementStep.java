package com.apress.batch.chapter10.config.step;

import com.apress.batch.chapter10.domain.customer.Customer;
import com.apress.batch.chapter10.domain.statement.Statement;
import com.apress.batch.chapter10.config.step.processor.AccountItemProcessor;
import com.apress.batch.chapter10.config.step.writer.StatementHeaderCallback;
import com.apress.batch.chapter10.config.step.writer.StatementLineAggregator;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class GenerateStatementStep {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step generateStatements(AccountItemProcessor itemProcessor) {
        return stepBuilderFactory.get("generateStatements")
            .<Statement, Statement>chunk(1)
            .reader(statementItemReader(null))
            .processor(itemProcessor)
            .writer(statementItemWriter(null))
            .build();
    }

    @Bean
    public JdbcCursorItemReader<Statement> statementItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Statement>()
            .name("statementItemReader")
            .dataSource(dataSource)
            .sql("SELECT * FROM customer")
            .rowMapper((rs, rowNum) -> {
                Customer customer = Customer
                    .builder()
                    .id(rs.getLong("customer_id"))
                    .firstName(rs.getString("first_name"))
                    .middleName(rs.getString("middle_name"))
                    .lastName(rs.getString("last_name"))
                    .address1(rs.getString("address1"))
                    .address2(rs.getString("address2"))
                    .city(rs.getString("city"))
                    .state(rs.getString("state"))
                    .postalCode(rs.getString("postal_code"))
                    .ssn(rs.getString("ssn"))
                    .emailAddress("email_address")
                    .homePhone("home_phone")
                    .cellPhone("cell_phone")
                    .workPhone("work_phone")
                    .notificationPreferences(rs.getInt("notification_pref"))
                    .build();

                return new Statement(customer);
            }).build();
    }

    @Bean
    @StepScope
    public MultiResourceItemWriter<Statement> statementItemWriter(
        @Value("#{jobParameters['outputDirectory']}") Resource outputDir) {
        return new MultiResourceItemWriterBuilder<Statement>()
            .name("statementItemWriter")
            .resource(outputDir)
            .itemCountLimitPerResource(1)
            .delegate(individualStatementItemWriter())
            .build();
    }

    @Bean
    public FlatFileItemWriter<Statement> individualStatementItemWriter() {
        FlatFileItemWriter<Statement> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setName("individualStatementItemWriter");
        itemWriter.setHeaderCallback(new StatementHeaderCallback());
        itemWriter.setLineAggregator(new StatementLineAggregator());

        return itemWriter;
    }

}
