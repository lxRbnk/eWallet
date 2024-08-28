package com.wallet.wallet.dto;

import com.wallet.wallet.model.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDto {

    private Long walletIdSender;

    @NotNull(message = "recipient ID cannot be null")
    private Long walletIdReceiver;

    @NotNull(message = "cannot be null")
    private BigDecimal amount;

    private Currency currency;
}
