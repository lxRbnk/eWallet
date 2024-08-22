package com.wallet.report.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletDto {
    private Long id;
    private Long ownerId;
    private String currency;
    private BigDecimal balance;
}
