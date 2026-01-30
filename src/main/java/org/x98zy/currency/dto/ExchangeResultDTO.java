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
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public Currency getBaseCurrency() { return baseCurrency; }
    public Currency getTargetCurrency() { return targetCurrency; }
    public double getRate() { return rate; }
    public double getAmount() { return amount; }
    public double getConvertedAmount() { return convertedAmount; }
}
