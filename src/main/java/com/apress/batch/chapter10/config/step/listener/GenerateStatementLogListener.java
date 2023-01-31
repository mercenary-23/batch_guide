package com.apress.batch.chapter10.config.step.listener;

import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import com.apress.batch.chapter10.domain.statement.Statement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenerateStatementLogListener {

    @OnReadError
    public void onReadError(Exception e) {
        log.error("GenerateStatementStep Reader Error has occurred", e);
        log.error("Error Message = {}", e.getMessage());
        e.printStackTrace();
    }

    @OnProcessError
    public void onProcessError(Statement item, Exception e) {
        log.error("GenerateStatementStep Processor Error has occurred", e);
        log.error("Statement in Processor = {}", item.toString());
        log.error("Error Message = {}", e.getMessage());
        e.printStackTrace();
    }

    @OnWriteError
    public void onWriteError(Exception e, List<? extends Statement> items) {
        log.error("GenerateStatementStep Writer Error has occurred", e);
        log.error("Statement Size in Writer = {}", items.size());
        for (Statement item : items) {
            log.error("Statement in Writer = {}", item.toString());
        }
        log.error("Error Message = {}", e.getMessage());
    }
}
