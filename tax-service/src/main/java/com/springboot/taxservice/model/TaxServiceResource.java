package com.springboot.taxservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TaxServiceResource {
    public static final String TRADER_ID = "traderId";
    public static final String PLAYED_AMOUNT = "playedAmount";
    public static final String ODD = "odd";

    @JsonProperty(TRADER_ID)
    private int traderId;
    @JsonProperty(PLAYED_AMOUNT)
    private BigDecimal playedAmount;
    @JsonProperty(ODD)
    private BigDecimal odd;

    public TaxServiceResource(@JsonProperty(value = TRADER_ID, required = true) int traderId, @JsonProperty(value = PLAYED_AMOUNT, required = true) BigDecimal playedAmount, @JsonProperty(value = ODD, required = true) BigDecimal odd) {
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
