package org.x98zy.currency;

import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.entity.Currency;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        CurrencyDAO dao = new CurrencyDAO();

        List<Currency> currencyList = dao.getAllCurrencies();
        System.out.println("Найдено валют " + currencyList.size());
        for(Currency c : currencyList) {
            System.out.println(c.getName() + " - " + c.getCode());
        }

        Currency newCurrency = dao.getCurrencyByCode("BYN");
        System.out.println(newCurrency.getName() + " " + newCurrency.getSign());

    }
}