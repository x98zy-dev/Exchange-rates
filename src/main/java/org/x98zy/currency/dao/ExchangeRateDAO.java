package org.x98zy.currency.dao;

import org.x98zy.currency.connection.ConnectionPool;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.entity.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO {

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        CurrencyDAO currencyDAO = new CurrencyDAO();

        String query = "SELECT id, \"BaseCurrencyId\", \"TargetCurrencyId\", rate FROM exchange_rates ORDER BY rate";

        Connection conn = ConnectionPool.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setId(resultSet.getLong("id"));
            exchangeRate.setBaseCurrency(currencyDAO.getCurrencyById(resultSet.getInt("BaseCurrencyId")));
            exchangeRate.setTargetCurrency(currencyDAO.getCurrencyById(resultSet.getInt("TargetCurrencyId")));
            exchangeRate.setRate(resultSet.getDouble("rate"));
            exchangeRates.add(exchangeRate);
            }
        return exchangeRates;
    }

    public ExchangeRate getExchangeRate(String baseCode, String targetCode) throws SQLException{

        ExchangeRate exchangeRate = new ExchangeRate();
        CurrencyDAO dao = new CurrencyDAO();
        String query = "SELECT id, BaseCurrencyId, TargetCurrencyId, rate FROM exchange_rates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        exchangeRate.setBaseCurrency(dao.getCurrencyByCode(baseCode));
        exchangeRate.setTargetCurrency(dao.getCurrencyByCode(targetCode));
        if (exchangeRate.getBaseCurrency() == null || exchangeRate.getTargetCurrency() == null)
        {
            return null;
        }
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setLong(1, exchangeRate.getBaseCurrency().getId());
        statement.setLong(2, exchangeRate.getTargetCurrency().getId());
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
        {
            exchangeRate.setId(resultSet.getLong("id"));
            // exchangeRate.setBaseCurrencyId(resultSet.getInt("base_currency_id"));
            // exchangeRate.setTargetCurrencyId(resultSet.getInt("target_currency_id"));
            exchangeRate.setRate(resultSet.getDouble("rate"));
            return exchangeRate;
        }
        else return null;
    }

    public ExchangeRate insertExchangeRate(Currency baseCurrency, Currency targetCurrency, double rate) throws SQLException  {

        String sql = "INSERT INTO exchange_rates (\"BaseCurrencyId\", \"TargetCurrencyId\", rate) VALUES (?, ?, ?) RETURNING id";

        try(Connection conn = ConnectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);) {

            statement.setInt(1, baseCurrency.getId().intValue());
            statement.setInt(2, targetCurrency.getId().intValue());
            statement.setDouble(3, rate);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int generatedId = resultSet.getInt("id");
                    return getExchangeRateById(generatedId);
                }
            }
        }
        return null;
    }

    public ExchangeRate getExchangeRateById(int id) {

        ExchangeRate exchangeRate = new ExchangeRate();
        CurrencyDAO dao = new CurrencyDAO();
        String query = "SELECT id, BaseCurrencyId, TargetCurrencyId, rate FROM exchange_rates WHERE id = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement statement = conn.prepareStatement(query))
        {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next())
                {
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
