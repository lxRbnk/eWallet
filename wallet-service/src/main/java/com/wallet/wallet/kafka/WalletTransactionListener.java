package com.wallet.wallet.kafka;

import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.model.Transaction;
import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.repository.TransactionalRepository;
import com.wallet.wallet.repository.WalletRepository;
import com.wallet.wallet.service.TransactionService;
import com.wallet.wallet.service.WalletService;
import com.wallet.wallet.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class WalletTransactionListener {

    private final WalletTransactionProducer producer;
    private final TransactionService transactionService;
    private final TransactionalRepository transactionalRepository;
    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private final JwtUtil jwtUtil;

    @KafkaListener(topics = "transaction-requests", groupId = "wallet-service-group")
    public void listenForTransactionRequests(String token) {
        List<Transaction> transactions = getTransactions(token);
        producer.sendTransactionResponse(transactions);
    }

    private List<Transaction> getTransactions(String token) {
        log.info(token);
        String login = jwtUtil.getLogin(token);
        return transactionalRepository.findAllByOwnerLogin(login);
    }
}
