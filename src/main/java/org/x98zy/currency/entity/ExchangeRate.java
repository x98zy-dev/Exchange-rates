package org.x98zy.currency.entity;

public class ExchangeRate {
    private Long id;
    private double rate;

    private Currency baseCurrency;
    private Currency targetCurrency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targerCurrency) {
        this.targetCurrency = targerCurrency;
    }

}
