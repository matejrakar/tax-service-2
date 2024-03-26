package com.springboot.taxservice.model;

import java.math.BigDecimal;

public class TaxServiceResource {
    private int traderId;
    private BigDecimal playedAmount;
    private BigDecimal odd;

    public TaxServiceResource(int traderId, BigDecimal playedAmount, BigDecimal odd) {
        this.traderId = traderId;
        this.playedAmount = playedAmount;
        this.odd = odd;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public BigDecimal getPlayedAmount() {
        return playedAmount;
    }

    public void setPlayedAmount(BigDecimal playedAmount) {
        this.playedAmount = playedAmount;
    }

    public BigDecimal getOdd() {
        return odd;
    }

    public void setOdd(BigDecimal odd) {
        this.odd = odd;
    }
}
