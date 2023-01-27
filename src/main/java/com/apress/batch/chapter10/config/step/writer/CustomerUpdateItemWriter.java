package com.apress.batch.chapter10.config.step.writer;

import com.apress.batch.chapter10.config.step.classifier.CustomerUpdateClassifier;
import com.apress.batch.chapter10.domain.customer.CustomerContactPreparedStatementSetter;
import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerUpdateItemWriter {
    @Bean
    public ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter() {
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(
            customerNameUpdateItemWriter(null),
            customerAddressUpdateItemWriter(null),
            customerContactUpdateItemWriter(null));

        ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(classifier);

        return compositeItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .beanMapped()
            .sql("UPDATE customer "
                + "SET first_name = COALESCE(:firstName, first_name), "
                + "middle_name = COALESCE(:middleName, middle_name), "
                + "last_name = COALESCE(:lastName, last_name) "
                + "WHERE customer_id = :customerId")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter(
        DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .beanMapped()
            .sql("UPDATE customer SET "
                + "address1 = COALESCE(:address1, address1), "
                + "address2 = COALESCE(:address2, address2), "
                + "city = COALESCE(:city, city), "
                + "state = COALESCE(:state, state), "
                + "postal_code = COALESCE(:postalCode, postal_code) "
                + "WHERE customer_id = :customerId")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter(
        DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
            .sql("UPDATE customer SET "
                + "email_address = COALESCE(?, email_address), "
                + "home_phone = COALESCE(?, home_phone), "
                + "cell_phone = COALESCE(?, cell_phone), "
                + "work_phone = COALESCE(?, work_phone), "
                + "notification_pref = COALESCE(?, notification_pref)"
                + "WHERE customer_id = ?")
            .dataSource(dataSource)
            .itemPreparedStatementSetter(new CustomerContactPreparedStatementSetter())
            .build();
    }
}
