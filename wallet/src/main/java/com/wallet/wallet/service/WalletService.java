package com.wallet.wallet.service;

import com.wallet.wallet.dto.*;
import com.wallet.wallet.model.Currency;
import com.wallet.wallet.model.Transaction;
import com.wallet.wallet.model.TransactionType;
import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.repository.TransactionalRepository;
import com.wallet.wallet.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionalRepository transactionalRepository;

    public void create(Wallet wallet) {
        walletRepository.save(wallet);
    }

    public void delete(Long id) {
        walletRepository.deleteById(id);
    }

    public WalletDto getWalletInfo(Long id) {
        Wallet wallet = walletRepository.getById(id);
        return toDto(wallet);
    }

    public List<Wallet> getWallets(Long ownerId) {
        return walletRepository.findAllByOwnerId(ownerId);
    }

    public void transfer(TransactionDto transferDto) {
//      fixme add check
        Wallet senderWallet = walletRepository.findById(transferDto.getWalletIdSender()).orElseThrow(); //fixme
        Wallet receiverWallet = walletRepository.findById(transferDto.getWalletIdReceiver()).orElseThrow(); //fixme

        BigDecimal amount = transferDto.getAmount();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .currency(transferDto.getCurrency())
                .walletSender(senderWallet)
                .walletReceiver(receiverWallet)
                .amount(amount)
                .timeStamp(LocalDateTime.now())
                .status(true)
                .build();

        if (!senderWallet.getCurrency().equals(receiverWallet.getCurrency())) {
            log.error("different currency");
            transaction.setStatus(false);
            transactionalRepository.save(transaction);
            return;
        }

        BigDecimal senderBalance = senderWallet.getBalance();
        BigDecimal receiverBalance = receiverWallet.getBalance();

        senderWallet.setBalance(senderBalance.subtract(amount));
        receiverWallet.setBalance(receiverBalance.add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
    }

    public void convert(ConvertDto convertDto) {
        Wallet walletFrom = walletRepository.findById(convertDto.getWalletIdFrom()).orElseThrow();
        Wallet walletTo = walletRepository.findById(convertDto.getWalletIdTo()).orElseThrow();
        BigDecimal amount = convertDto.getAmount();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.CONVERT)
                .walletSender(walletFrom)
                .walletReceiver(walletTo)
                .amount(amount)
                .currency(walletFrom.getCurrency())
                .currencyTo(walletTo.getCurrency())
                .timeStamp(LocalDateTime.now())
                .status(true)
                .build();
//        transaction.setStatus(false); //fixme check

        BigDecimal balanceFrom = walletFrom.getBalance();
        BigDecimal balanceTo = walletTo.getBalance();
        walletFrom.setBalance(balanceFrom.subtract(amount));
        BigDecimal converted = convertTo(amount, walletFrom.getCurrency(), walletTo.getCurrency());
        walletTo.setBalance(balanceTo.add(converted));

        walletRepository.save(walletFrom);
        walletRepository.save(walletTo);
        transactionalRepository.save(transaction);
    }

    public void topUp(TopUpDto topUpDto) {
        Wallet wallet = walletRepository.findById(topUpDto.getWalletId()).orElseThrow();
        BigDecimal balance = wallet.getBalance();
        wallet.setBalance(balance.add(topUpDto.getAmount()));
        walletRepository.save(wallet);
        Transaction transaction = Transaction.builder()
                .walletSender(wallet)
                .walletReceiver(wallet)
                .type(TransactionType.TOP_UP)
                .amount(topUpDto.getAmount())
                .currency(wallet.getCurrency())
                .timeStamp(LocalDateTime.now())
                .status(true)
                .build();
        transactionalRepository.save(transaction);
    }

    public void withdrawal(WithdrawalDto withdrawalDto) {
        Wallet wallet = walletRepository.findById(withdrawalDto.getWalletId()).orElseThrow();
        BigDecimal balance = wallet.getBalance();
        wallet.setBalance(balance.subtract(withdrawalDto.getAmount()));
        walletRepository.save(wallet);
        Transaction transaction = Transaction.builder()
                .walletSender(wallet)
                .walletReceiver(wallet)
                .type(TransactionType.WITHDRAWAL)
                .amount(withdrawalDto.getAmount())
                .currency(wallet.getCurrency())
                .timeStamp(LocalDateTime.now())
                .status(true)
                .build();
        transactionalRepository.save(transaction);
    }

    private BigDecimal convertTo(BigDecimal amount, Currency from, Currency to) {
        return to.getMultiplier()
                .divide(from.getMultiplier(), 3, RoundingMode.HALF_UP)
                .multiply(amount);
    }

    private WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
                .id(wallet.getId())
                .ownerId(wallet.getOwnerId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency().toString())
                .build();
    }

//    private Wallet toEntity(){
//        return new Wallet();
//    }
}
