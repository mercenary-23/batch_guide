package com.apress.batch.chapter10.config.step.classifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.apress.batch.chapter10.domain.customer.CustomerAddressUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerContactUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerNameUpdate;
import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

class CustomerUpdateClassifierTest {

    private JdbcBatchItemWriter<CustomerUpdate> recordType1;
    private JdbcBatchItemWriter<CustomerUpdate> recordType2;
    private JdbcBatchItemWriter<CustomerUpdate> recordType3;

    @BeforeEach
    void setUp() {
        recordType1 = new JdbcBatchItemWriter<>();
        recordType2 = new JdbcBatchItemWriter<>();
        recordType3 = new JdbcBatchItemWriter<>();
    }

    @Test
    @DisplayName("CustomerUpdateClassifier Test - CustomerNameUpdate")
    void testClassifyType1() {
        //given
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(recordType1, recordType2,
            recordType3);
        CustomerNameUpdate customerNameUpdate = new CustomerNameUpdate(1L, "first", "middle",
            "last");

        //when & then
        assertThat(classifier.classify(customerNameUpdate)).isEqualTo(recordType1);
    }

    @Test
    @DisplayName("CustomerUpdateClassifier Test - CustomerAddressUpdate")
    void testClassifyType2() {
        //given
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(recordType1, recordType2,
            recordType3);
        CustomerAddressUpdate customerAddressUpdate = CustomerAddressUpdate.builder()
            .customerId(2L)
            .build();

        //when & then
        assertThat(classifier.classify(customerAddressUpdate)).isEqualTo(recordType2);
    }

    @Test
    @DisplayName("CustomerUpdateClassifier Test - CustomerContactUpdate")
    void testClassifyType3() {
        //given
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(recordType1, recordType2,
            recordType3);
        CustomerContactUpdate customerContactUpdate = CustomerContactUpdate.builder()
            .customerId(3L)
            .build();

        //when & then
        assertThat(classifier.classify(customerContactUpdate)).isEqualTo(recordType3);
    }

    @Test
    @DisplayName("CustomerUpdateClassifier Test - Invalid Type")
    void testClassifyInvalidType() {
        //given
        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(recordType1, recordType2,
            recordType3);

        //when & then
        assertThatThrownBy(() -> classifier.classify(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Type");
    }
}