package com.apress.batch.chapter10.config.step;

import com.apress.batch.chapter10.config.step.listener.CustomerItemListener;
import com.apress.batch.chapter10.config.step.writer.CustomerUpdateItemWriter;
import com.apress.batch.chapter10.domain.customer.CustomerAddressUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerContactUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerNameUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import com.apress.batch.chapter10.config.step.validator.CustomerItemValidator;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
public class ImportCustomerStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerUpdateItemWriter customerUpdateItemWriter;

    @Bean
    public Step importCustomerUpdates() throws Exception {
        return stepBuilderFactory.get("importCustomerUpdates")
            .<CustomerUpdate, CustomerUpdate>chunk(100)
            .reader(customerUpdateItemReader(null))
            .processor(customerValidatingItemProcessor(null))
            .writer(customerUpdateItemWriter.compositeItemWriter())
            .faultTolerant()
            .skip(Exception.class)
            .skipLimit(10)
            .listener(new CustomerItemListener())
            .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerUpdate> customerUpdateItemReader(
        @Value("#{jobParameters['customerUpdateFile']}") Resource inputFile
    ) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
            .name("customerUpdateItemReader")
            .resource(inputFile)
            .lineTokenizer(customerUpdatesLineTokenizer())
            .fieldSetMapper(customerUpdateFieldSetMapper())
            .build();
    }

    @Bean
    public LineTokenizer customerUpdatesLineTokenizer() throws Exception {
        DelimitedLineTokenizer recordType1 = new DelimitedLineTokenizer();
        setTokenizerNames(recordType1, "recordId", "customerId", "firstName", "middleName",
            "lastName");

        DelimitedLineTokenizer recordType2 = new DelimitedLineTokenizer();
        setTokenizerNames(recordType2, "recordId", "customerId", "address1", "address2", "city", "state",
            "postalCode");

        DelimitedLineTokenizer recordType3 = new DelimitedLineTokenizer();
        setTokenizerNames(recordType3, "recordId", "customerId", "emailAddress", "homePhone", "cellPhone",
            "workPhone", "notificationPreference");

        Map<String, LineTokenizer> tokenizers = new HashMap<>();
        tokenizers.put("1*", recordType1);
        tokenizers.put("2*", recordType2);
        tokenizers.put("3*", recordType3);

        PatternMatchingCompositeLineTokenizer lineTokenizer = new PatternMatchingCompositeLineTokenizer();
        lineTokenizer.setTokenizers(tokenizers);

        return lineTokenizer;
    }

    private void setTokenizerNames(DelimitedLineTokenizer lineTokenizer, String... names)
        throws Exception {
        lineTokenizer.setNames(names);
        lineTokenizer.afterPropertiesSet();
    }

    @Bean
    public FieldSetMapper<CustomerUpdate> customerUpdateFieldSetMapper() {
        return fieldSet -> {
            switch (fieldSet.readInt("recordId")) {
                case 1 -> {
                    return CustomerNameUpdate
                        .builder()
                        .customerId(fieldSet.readLong("customerId"))
                        .firstName(fieldSet.readString("firstName"))
                        .middleName(fieldSet.readString("middleName"))
                        .lastName(fieldSet.readString("lastName"))
                        .build();
                }
                case 2 -> {
                    return CustomerAddressUpdate
                        .builder()
                        .customerId(fieldSet.readLong("customerId"))
                        .address1(fieldSet.readString("address1"))
                        .address2(fieldSet.readString("address2"))
                        .city(fieldSet.readString("city"))
                        .state(fieldSet.readString("state"))
                        .postalCode(fieldSet.readString("postalCode"))
                        .build();
                }
                case 3 -> {
                    String rawPreference = fieldSet.readString("notificationPreference");
                    Integer notificationPreference = null;
                    if (StringUtils.hasText(rawPreference)) {
                        notificationPreference = Integer.parseInt(rawPreference);
                    }
                    return CustomerContactUpdate
                        .builder()
                        .customerId(fieldSet.readLong("customerId"))
                        .emailAddress(fieldSet.readString("emailAddress"))
                        .homePhone(fieldSet.readString("homePhone"))
                        .cellPhone(fieldSet.readString("cellPhone"))
                        .workPhone(fieldSet.readString("workPhone"))
                        .notificationPreferences(notificationPreference)
                        .build();
                }
                default -> throw new IllegalArgumentException(
                    "Invalid record type was found:" + fieldSet.readInt("recordId"));
            }
        };
    }

    @Bean
    public ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor(
        CustomerItemValidator validator) {
        ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor = new ValidatingItemProcessor<>(
            validator);

        customerValidatingItemProcessor.setFilter(true);

        return customerValidatingItemProcessor;
    }

}
