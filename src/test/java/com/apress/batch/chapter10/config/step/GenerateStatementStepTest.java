package com.apress.batch.chapter10.config.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.test.AssertFile.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBatchTest
@SpringBootTest
@Sql("classpath:schema.sql")
@Sql("classpath:data-test.sql")
@Sql("classpath:transaction.sql")
public class GenerateStatementStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("GenerateStatementStep TEST")
    void testGenerateStatementStep() throws Exception {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("outputDirectory",
                "file:///C:/Users/DIR-N-156/Desktop/java/batch/chapter10/src/test/resources/output/out")
            .toJobParameters();

        //when
        JobExecution jobExecution =
            this.jobLauncherTestUtils.launchStep("generateStatements",
                jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        String output = "src/test/resources/output/out.1";
        String expected = "src/test/resources/expected/expected.1";
        assertFileEquals(new FileSystemResource(expected), new FileSystemResource(output));
    }
}
