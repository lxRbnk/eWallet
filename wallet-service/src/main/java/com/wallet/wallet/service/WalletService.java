package com.wallet.wallet.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wallet.wallet.dto.*;
import com.wallet.wallet.model.Currency;
import com.wallet.wallet.model.Transaction;
import com.wallet.wallet.model.TransactionType;
import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.repository.TransactionalRepository;
import com.wallet.wallet.repository.WalletRepository;
import com.wallet.wallet.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionalRepository transactionalRepository;
    private final JwtUtil jwtUtil;

    public boolean create(WalletDto walletDto, String token) {
        try {
            String login = jwtUtil.getLogin(token);
            Wallet wallet = toEntity(walletDto);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setOwnerLogin(login);
            walletRepository.save(wallet);
            return true;
        } catch (NullPointerException |
                 JWTVerificationException e) {
            return false;
        }
    }

    @Transactional
    public boolean delete(Long id) { //method for admin
        if (!walletRepository.existsById(id)) {
            return false;
        }
        walletRepository.deleteById(id);
        return true;
    }

    public WalletDto getWalletInfo(Long id) {
        if (walletRepository.existsById(id)) {
            Wallet wallet = walletRepository.getById(id);
            return toDto(wallet);
        }
        return null;
    }

    public List<WalletDto> getWallets(String token) {
        String login = jwtUtil.getLogin(token);
        List<Wallet> listWallet = walletRepository.findAllByOwnerLogin(login);
        return listWallet.stream().map(this::toDto).toList();
    }

    public String transfer(TransactionDto transferDto, String token) {
        String login = jwtUtil.getLogin(token);
        Wallet senderWallet = walletRepository.findById(transferDto.getWalletIdSender()).orElseThrow();
        if(!senderWallet.getOwnerLogin().equals(login)){
            log.error("choose your wallet");
            return null;
        }
        Wallet receiverWallet = walletRepository.findById(transferDto.getWalletIdReceiver()).orElseThrow();
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
            return null;
        }
        BigDecimal senderBalance = senderWallet.getBalance();
        BigDecimal receiverBalance = receiverWallet.getBalance();
        senderWallet.setBalance(senderBalance.subtract(amount));
        receiverWallet.setBalance(receiverBalance.add(amount));
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        transactionalRepository.save(transaction);
        return "completed";
    }

    public boolean convert(ConvertDto convertDto) {
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
        if(walletFrom.getBalance().compareTo(amount) < 0){
            transaction.setStatus(false);
            transactionalRepository.save(transaction);
            log.warn("insufficient funds");
            return false;
        }
        BigDecimal balanceFrom = walletFrom.getBalance();
        BigDecimal balanceTo = walletTo.getBalance();
        walletFrom.setBalance(balanceFrom.subtract(amount));
        BigDecimal converted = convertTo(amount, walletFrom.getCurrency(), walletTo.getCurrency());
        walletTo.setBalance(balanceTo.add(converted));
        walletRepository.save(walletFrom);
        walletRepository.save(walletTo);
        transactionalRepository.save(transaction);
        return true;
    }

    public boolean topUp(TopUpDto topUpDto) {
        Wallet wallet;
        Transaction transaction;
        try {
            wallet = walletRepository.findById(topUpDto.getWalletId())
                    .orElseThrow();
            BigDecimal balance = wallet.getBalance();
            wallet.setBalance(balance.add(topUpDto.getAmount()));
            walletRepository.save(wallet);
            transaction = Transaction.builder()
                    .walletSender(wallet)
                    .walletReceiver(wallet)
                    .type(TransactionType.TOP_UP)
                    .amount(topUpDto.getAmount())
                    .currency(wallet.getCurrency())
                    .timeStamp(LocalDateTime.now())
                    .status(true)
                    .build();
        }catch (NullPointerException |
                NoSuchElementException e){
            return false;
        }
        transactionalRepository.save(transaction);
        return true;
    }

    public boolean withdrawal(WithdrawalDto withdrawalDto, String token) {
        String login = jwtUtil.getLogin(token);
        BigDecimal amount = withdrawalDto.getAmount();
        Wallet wallet;
        Transaction transaction = null;
        try{
            wallet = walletRepository.findById(withdrawalDto.getWalletId()).orElseThrow();
        }catch (NoSuchElementException e){
            return false;
        }
        if(!wallet.getOwnerLogin().equals(login)){
            return false;
        }
        BigDecimal balance = wallet.getBalance();
        transaction = Transaction.builder()
                .walletSender(wallet)
                .walletReceiver(wallet)
                .type(TransactionType.WITHDRAWAL)
                .amount(withdrawalDto.getAmount())
                .currency(wallet.getCurrency())
                .timeStamp(LocalDateTime.now())
                .status(true)
                .build();
        if(balance.compareTo(amount) < 0 ){
            transaction.setStatus(false);
            transactionalRepository.save(transaction);
            log.warn("insufficient funds");
            return false;
        }
        wallet.setBalance(balance.subtract(withdrawalDto.getAmount()));
        walletRepository.save(wallet);
        transactionalRepository.save(transaction);
        return true;
    }

    private BigDecimal convertTo(BigDecimal amount, Currency from, Currency to) {
        return to.getMultiplier()
                .divide(from.getMultiplier(), 3, RoundingMode.HALF_UP)
                .multiply(amount);
    }

    private WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
                .ownerLogin(wallet.getOwnerLogin())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency().toString())
                .build();
    }

    private Wallet toEntity(WalletDto walletDto) {
        try {
            Currency currency = Currency.valueOf(walletDto.getCurrency());
            Wallet wallet = Wallet.builder()
                    .currency(currency)
                    .build();
            return wallet;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
