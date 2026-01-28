package org.x98zy.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.x98zy.currency.dao.ExchangeRateDAO;
import org.x98zy.currency.entity.ExchangeRate;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateDAO dao = new ExchangeRateDAO();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");

        try {
            List<ExchangeRate> exchangeRates = dao.getAllExchangeRates();
            res.getWriter().write(mapper.writeValueAsString(exchangeRates));
        } catch (IOException e) {
            System.out.println("Error");
        }


    }
}
