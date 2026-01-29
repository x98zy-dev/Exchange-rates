package org.x98zy.currency.service;

import org.x98zy.currency.dao.ExchangeRateDAO;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.entity.ExchangeRate;
import org.x98zy.currency.exeption.ServiceExeption;

import java.sql.SQLException;

public class AddExchangeRateService {

    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public ExchangeRate insertExchangeRate (Currency baseCurrency, Currency targetCurrency, double rate) throws ServiceExeption{

        try {
            return exchangeRateDAO.insertExchangeRate(baseCurrency, targetCurrency, rate);
        } catch (SQLException e) {
            if("23505".equals(e.getSQLState())) {
                throw new ServiceExeption("Валютная пара с таким кодом уже существует", 409);
            }
            else if("23503".equals(e.getSQLState())) {
                throw new ServiceExeption("Одна (или обе) валюта из валютной пары не существуетв БД", 404);
            }
            else {
                new ServiceExeption("Ошибка базы данных", 500, e);
            }
        }

        return null;
    }
}
