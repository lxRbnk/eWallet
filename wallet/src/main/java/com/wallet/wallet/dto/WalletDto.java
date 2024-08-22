package com.wallet.wallet.dto;

import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WalletDto {
    private Long id;
    private Long ownerId;
    private String currency;
    private BigDecimal balance;

}
