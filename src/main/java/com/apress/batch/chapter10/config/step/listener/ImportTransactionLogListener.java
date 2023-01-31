package com.apress.batch.chapter10.config.step.listener;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImportTransactionLogListener {
    @OnReadError
    public void onReadError(Exception e) {
        log.error("ImportTransactionStep Reader Error has occurred", e);
        log.error("Error Message = {}", e.getMessage());
        e.printStackTrace();
    }

    @OnWriteError
    public void onWriteError(Exception e, List<? extends Transaction> items) {
        log.error("ImportTransactionStep Writer Error has occurred", e);
        log.error("Transaction Size in Writer = {}", items.size());
        for (Transaction item : items) {
            log.error("Transaction in Writer = {}", item.toString());
        }
        log.error("Error Message = {}", e.getMessage());
    }
}
