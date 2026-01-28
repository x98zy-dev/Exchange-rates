package org.x98zy.currency.dao;

import org.x98zy.currency.connection.ConnectionPool;
import org.x98zy.currency.entity.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO {

    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        String query = "SELECT id, \"BaseCurrencyId\", \"TargetCurrencyId\", rate FROM exchange_rates ORDER BY rate";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getLong("id"));
                exchangeRate.setBaseCurrencyId(resultSet.getInt("BaseCurrencyId"));
                exchangeRate.setTargetCurrencyId(resultSet.getInt("TargetCurrencyId"));
                exchangeRate.setRate(resultSet.getDouble("rate"));
                exchangeRates.add(exchangeRate);
            }

        } catch (SQLException e) {
            // ВАЖНО: выводим полную информацию об ошибке
            System.err.println("SQL error in getAllExchangeRates: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace(); // полный стектрейс
        }

        return exchangeRates;
    }

    public ExchangeRate getExchangeRate(String baseCode, String targetCode) {

        ExchangeRate exchangeRate = new ExchangeRate();
        CurrencyDAO dao = new CurrencyDAO();
        String query = "SELECT id, BaseCurrencyId, TargetCurrencyId, rate FROM exchange_rates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        exchangeRate.setBaseCurrency(dao.getCurrencyByCode(baseCode));
        exchangeRate.setTargetCurrency(dao.getCurrencyByCode(targetCode));
        if (exchangeRate.getBaseCurrency() == null || exchangeRate.getTargetCurrency() == null)
        {
            return null;
        }
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement statement = conn.prepareStatement(query))
        {
             statement.setLong(1, exchangeRate.getBaseCurrency().getId());
             statement.setLong(2, exchangeRate.getTargetCurrency().getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next())
                {
                    exchangeRate.setId(resultSet.getLong("id"));
                    // exchangeRate.setBaseCurrencyId(resultSet.getInt("base_currency_id"));
                    // exchangeRate.setTargetCurrencyId(resultSet.getInt("target_currency_id"));
                    exchangeRate.setRate(resultSet.getDouble("rate"));
                    return exchangeRate;
                }

            } catch (SQLException e){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            System.out.println("error");
        }
        return null;
    }

}
