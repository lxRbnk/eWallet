package com.wallet.wallet.model;

import java.math.BigDecimal;

public enum Currency {
    USD(BigDecimal.valueOf(1)),
    EUR(BigDecimal.valueOf(0.9)),
    GBP(BigDecimal.valueOf(0.8)),
    CNY(BigDecimal.valueOf(7)),
    RUB(BigDecimal.valueOf(10));

    final BigDecimal multiplier;

    Currency(BigDecimal multiplier){
        this.multiplier = multiplier;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}
