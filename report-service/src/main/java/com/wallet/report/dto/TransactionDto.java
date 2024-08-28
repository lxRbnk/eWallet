package com.wallet.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private String type;
    private WalletDto walletSender;
    private WalletDto walletReceiver;
    private BigDecimal amount;
    private String currency;
    private String currencyTo;
    private LocalDateTime timeStamp;
    private boolean status;
}
