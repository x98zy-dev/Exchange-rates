package org.x98zy.currency.dao;

import org.x98zy.currency.connection.ConnectionPool;
import org.x98zy.currency.entity.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {

    public List<Currency> getAllCurrencies() throws SQLException {

        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT id, code, name, sign FROM currencies ORDER BY code";
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement stmnt = conn.prepareStatement(sql);
        ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            Currency currency = new Currency();
            currency.setId(rs.getLong("id"));
            currency.setName(rs.getString("name"));
            currency.setCode(rs.getString("code"));
            currency.setSign(rs.getString("sign"));
            currencies.add(currency);
        }
        return currencies;
    }

    public Currency getCurrencyByCode(String code) throws SQLException{

        String sql = "SELECT id, code, name, sign FROM currencies WHERE code = ?";
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement query = conn.prepareStatement(sql);
        query.setString(1, code.toUpperCase());

        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            Currency currency = new Currency();
            currency.setId(rs.getLong("id"));
            currency.setName(rs.getString("name"));
            currency.setCode(rs.getString("code"));
            currency.setSign(rs.getString("sign"));
            return currency;
        }
        else return null;
    }

    public Currency getCurrencyById(int id) throws SQLException {

        String sql = "SELECT id, code, name, sign FROM currencies WHERE id = ?";
        Connection conn = ConnectionPool.getConnection();
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            Currency currency = new Currency();
            currency.setId(rs.getLong("id"));
            currency.setName(rs.getString("name"));
            currency.setCode(rs.getString("code"));
            currency.setSign(rs.getString("sign"));
            return currency;
        }
        else return null;
    }

    public int insertCurrency(String code, String name, String sign) throws SQLException {

        String sql = "INSERT INTO currencies (code, name, sign) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = ConnectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, code);
            statement.setString(2, name);
            statement.setString(3, sign);

            try(ResultSet resultSet = statement.executeQuery())
            {
                if(resultSet.next()) {
                    int generatedId = resultSet.getInt("id");
                    return generatedId;
                }
            }
            }
        return -1;
        }

    }

