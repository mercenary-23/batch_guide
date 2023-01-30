package com.apress.batch.chapter10.domain.transaction;

import static com.apress.batch.chapter10.constant.DatePatternConst.TRANSACTION_DATE_PATTERN;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JaxbDateSerializer extends XmlAdapter<String, LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TRANSACTION_DATE_PATTERN);

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        return date.format(formatter);
    }

    @Override
    public LocalDateTime unmarshal(String date) throws Exception {
        return LocalDateTime.parse(date, formatter);
    }
}
