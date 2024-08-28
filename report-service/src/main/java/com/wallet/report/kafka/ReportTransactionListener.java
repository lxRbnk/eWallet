package com.wallet.report.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.report.dto.TransactionDto;
import com.wallet.report.service.ReportService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportTransactionListener {

    private final ObjectMapper objectMapper;
    private final ReportService reportService;

    @KafkaListener(topics = "transaction-responses", groupId = "report-service-group")
    public void handleTransactionResponse(ConsumerRecord<String, String> record) {
        try {
            List<TransactionDto> transactions = objectMapper.readValue(
                    record.value(),
                    new TypeReference<List<TransactionDto>>() {}
            );
            reportService.addTransactionsToQueue(transactions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
