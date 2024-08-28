package com.wallet.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WalletDto {

    private String ownerLogin;

    @NotNull(message = "currency cannot be null")
    private String currency;

    private BigDecimal balance;

}
