package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;

import com.apress.batch.chapter10.domain.statement.Customer;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBatchTest
@SpringBootTest
@Sql("classpath:data-test.sql")
public class ImportCustomerStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("ImportCustomerStep TEST")
    public void testImportCustomerStep() {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("customerUpdateFile", "classpath:customerUpdateFile.csv")
            .toJobParameters();

        //when
        JobExecution jobExecution =
            this.jobLauncherTestUtils.launchStep("importCustomerUpdates",
                jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        List<Customer> results = jdbcTemplate.query("select * from customer where customer_id = 501",
            (rs, rowNum) -> {
                return Customer.builder()
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
                    .emailAddress(rs.getString("email_address"))
                    .homePhone(rs.getString("home_phone"))
                    .cellPhone(rs.getString("cell_phone"))
                    .workPhone(rs.getString("work_phone"))
                    .notificationPreferences(rs.getInt("notification_pref"))
                    .build();
            });
        Customer customer = results.get(0);

        assertThat(customer.getId()).isEqualTo(501);
        assertThat(customer.getFirstName()).isEqualTo("Rozelle");
        assertThat(customer.getMiddleName()).isEqualTo("Heda");
        assertThat(customer.getLastName()).isEqualTo("Farnill");
        assertThat(customer.getAddress1()).isEqualTo("36 Ronald Regan Terrace");
        assertThat(customer.getAddress2()).isEqualTo("P.O. Box 33");
        assertThat(customer.getCity()).isEqualTo("Montgomery");
        assertThat(customer.getState()).isEqualTo("Alabama");
        assertThat(customer.getPostalCode()).isEqualTo("36134");
        assertThat(customer.getSsn()).isEqualTo("832-86-3661");
        assertThat(customer.getEmailAddress()).isEqualTo("tlangelay4@mac.com");
        assertThat(customer.getHomePhone()).isEqualTo("240-906-7652");
        assertThat(customer.getCellPhone()).isEqualTo("907-709-2649");
        assertThat(customer.getWorkPhone()).isEqualTo("316-510-9138");
        assertThat(customer.getNotificationPreferences()).isEqualTo(2);
    }
}
