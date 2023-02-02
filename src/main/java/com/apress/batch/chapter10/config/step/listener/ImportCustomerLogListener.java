package com.apress.batch.chapter10.config.step.listener;

import com.apress.batch.chapter10.domain.customer.CustomerUpdate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImportCustomerLogListener {

    @OnReadError
    public void onReadError(Exception e) {
        log.error("ImportCustomerStep Reader Error has occurred", e);
    }

    @OnProcessError
    public void onProcessError(CustomerUpdate item, Exception e) {
        log.error("ImportCustomerStep Processor Error has occurred", e.toString());
        log.error("CustomerUpdate in Processor = {}", item.toString());
    }

    @OnWriteError
    public void onWriteError(Exception e, List<? extends CustomerUpdate> items) {
        log.error("ImportCustomerStep Writer Error has occurred", e.toString());
        log.error("CustomerUpdate Size in Writer = {}", items.size());
        for (CustomerUpdate item : items) {
            log.error("CustomerUpdate in Writer = {}", item.toString());
        }
    }

}
