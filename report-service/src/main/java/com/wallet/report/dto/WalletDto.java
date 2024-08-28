package com.wallet.report.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletDto {
    private Long id;
    private String currency;
}
