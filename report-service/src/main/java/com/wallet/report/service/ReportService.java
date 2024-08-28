package com.wallet.report.service;

import com.wallet.report.dto.TransactionDto;
import com.wallet.report.kafka.ReportTransactionListener;
import com.wallet.report.kafka.ReportTransactionProducer;
import com.wallet.report.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReportTransactionProducer producer;
    private final BlockingQueue<List<TransactionDto>> responseQueue = new LinkedBlockingQueue<>();

    public List<TransactionDto> getUserTransactions(String token) {
        producer.requestTransactions(token);
        try {
            List<TransactionDto> transactions = responseQueue.poll(3, TimeUnit.SECONDS);
            if (transactions != null) {
                return transactions;
            } else {
                System.out.println("Timeout occurred while waiting for transaction response");
                return new ArrayList<>();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for transaction response", e);
        }
    }

    public void addTransactionsToQueue(List<TransactionDto> transactions) {
        responseQueue.offer(transactions);
    }
}
