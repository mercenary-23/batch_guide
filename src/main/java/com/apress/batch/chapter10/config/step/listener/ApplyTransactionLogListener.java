package com.apress.batch.chapter10.config.step.listener;

import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplyTransactionLogListener {

    @OnReadError
    public void onReadError(Exception e) {
        log.error("ApplyTransactionStep Reader Error has occurred", e);
    }

    @OnWriteError
    public void onWriteError(Exception e, List<? extends Transaction> items) {
        log.error("ApplyTransactionStep Writer Error has occurred", e.toString());
        log.error("Transaction Size in Writer = {}", items.size());
        for (Transaction item : items) {
            log.error("Transaction in Writer = {}", item.toString());
        }
    }
}
