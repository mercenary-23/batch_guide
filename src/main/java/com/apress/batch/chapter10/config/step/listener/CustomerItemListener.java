package com.apress.batch.chapter10.config.step.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;

@Slf4j
public class CustomerItemListener {

    @OnReadError
    public void onReadError(Exception e) {
        log.error("Error has occurred", e);
        log.error("Error Message = {}", e.getMessage());
        e.printStackTrace();
    }
}
