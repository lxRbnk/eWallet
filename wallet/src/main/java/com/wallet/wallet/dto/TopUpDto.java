package com.wallet.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TopUpDto {
    private BigDecimal amount;
    private Long walletId;
}
