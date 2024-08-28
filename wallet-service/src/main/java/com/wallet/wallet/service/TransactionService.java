package com.wallet.wallet.service;

import com.wallet.wallet.model.Transaction;
import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.repository.TransactionalRepository;
import com.wallet.wallet.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionalRepository transactionalRepository;
    private final WalletRepository walletRepository;

    public List<Transaction> getUserTransactions(String login){

        return null;
    }

}
