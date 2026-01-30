package org.x98zy.currency.service;

import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.dao.ExchangeRateDAO;
import org.x98zy.currency.dto.ExchangeResultDTO;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.entity.ExchangeRate;
import org.x98zy.currency.exeption.*;
import org.x98zy.currency.validation.Validator;

import java.sql.SQLException;

public class ExchangeService {

    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final Validator validator = new Validator();

    public ExchangeResultDTO exchangeCurrencies(String baseCurrencyCode, String targetCurrencyCode, String stringAmount) throws ServiceExeption{

        ExchangeRate exchangeRate;
        double rate = 0;
        double amount;
        if (!validator.isValidCurrencyCode(baseCurrencyCode) || !validator.isValidCurrencyCode(targetCurrencyCode)) {
            throw new InvalidDataExeption();
        }

        try {
            exchangeRate = exchangeRateDAO.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            if(exchangeRate != null)
                rate = exchangeRate.getRate();
            else {
                exchangeRate = exchangeRateDAO.getExchangeRate(targetCurrencyCode, baseCurrencyCode);
                {
                    if(exchangeRate != null)
                    {
                        rate = 1/exchangeRate.getRate();
                    }
                    else {
                        ExchangeRate A = exchangeRateDAO.getExchangeRate("USD", baseCurrencyCode);
                        ExchangeRate B = exchangeRateDAO.getExchangeRate("USD", targetCurrencyCode);
                        if(A != null && B != null)
                            rate = B.getRate()/A.getRate();
                    }
                }
            }
            if(rate == 0) {
                throw new ExchangeRateNotFoundExeption();
            }
            try {
                amount = Double.parseDouble(stringAmount);
                if (amount <= 0) {
                    throw new InvalidAmountExeption();
                }
                double calculatedAmount = amount * rate;
                Currency baseCurrency = currencyDAO.getCurrencyByCode(baseCurrencyCode);
                Currency targetCurrency = currencyDAO.getCurrencyByCode(targetCurrencyCode);
                return new ExchangeResultDTO(baseCurrency, targetCurrency, rate, amount, calculatedAmount);
            } catch (NumberFormatException e) {
                throw new ServiceExeption();
            }
        } catch (SQLException e) {
            throw new ServiceExeption();
        }
    }
}
