package com.wallet.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ConvertDto {

    @NotNull(message = "amount annot be null")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "wallet Id From cannot be null")
    private Long walletIdFrom;

    @NotNull(message = "wallet Id To cannot be null")
    private Long walletIdTo;
}
