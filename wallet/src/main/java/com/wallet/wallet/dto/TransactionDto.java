package com.wallet.wallet.dto;

import com.wallet.wallet.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDto {
    private Long walletIdSender ;
    private Long walletIdReceiver;
    private BigDecimal amount;
    private Currency currency;
}
