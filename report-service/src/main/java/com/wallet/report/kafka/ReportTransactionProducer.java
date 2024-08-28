package com.wallet.report.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportTransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ReportTransactionProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void requestTransactions(String token) {
        kafkaTemplate.send("transaction-requests", token);
    }
}
