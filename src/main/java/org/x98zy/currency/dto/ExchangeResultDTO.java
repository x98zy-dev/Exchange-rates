package org.x98zy.currency.dto;

import org.x98zy.currency.entity.Currency;

public class ExchangeResultDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeResultDTO(Currency baseCurrency, Currency targetCurrency,
                             double rate, double amount, double convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate =  Math.round(rate * 100.0) / 100.0;
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.convertedAmount = Math.round(convertedAmount * 100.0) / 100.0;;
    }

    public Currency getBaseCurrency() { return baseCurrency; }
    public Currency getTargetCurrency() { return targetCurrency; }
    public double getRate() { return rate; }
    public double getAmount() { return amount; }
    public double getConvertedAmount() { return convertedAmount; }
}
