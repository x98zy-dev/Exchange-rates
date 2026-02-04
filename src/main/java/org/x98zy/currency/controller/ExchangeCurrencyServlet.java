package org.x98zy.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.dto.ExchangeResultDTO;
import org.x98zy.currency.exeption.ServiceExeption;
import org.x98zy.currency.service.ExchangeService;

import java.io.IOException;

@WebServlet ("/exchange")
public class ExchangeCurrencyServlet extends HttpServlet {

    private final ExchangeService exchangeService = new ExchangeService();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String stringAmount = req.getParameter("amount");
        try {
            ExchangeResultDTO resultDTO = exchangeService.exchangeCurrencies(baseCurrencyCode, targetCurrencyCode, stringAmount);
            res.getWriter().write(mapper.writeValueAsString(resultDTO));
        } catch (ServiceExeption e) {
            System.out.println("serviceExeption: " + e);
            setMessage(res, 500, "Внутренняя ошибка сервера");
        } catch (Exception exeption) {
            System.out.println("exeption: " + exeption);
            setMessage(res, 500,"Внутренняя ошибка сервера");
        }

    }

    public void setMessage (HttpServletResponse res, int statusCode, String message) throws IOException {

        res.setStatus(statusCode);
        res.setContentType("application/json;charset=UTF-8");

        String json = String.format("{\"message\":\"%s\"}", message);
        res.getWriter().write(json);
    }
}
