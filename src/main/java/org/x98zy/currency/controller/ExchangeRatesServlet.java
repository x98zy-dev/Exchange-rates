package org.x98zy.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.dao.ExchangeRateDAO;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.entity.ExchangeRate;
import org.x98zy.currency.exeption.ServiceExeption;
import org.x98zy.currency.service.AddExchangeRateService;
import org.x98zy.currency.validation.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateDAO ratesDao = new ExchangeRateDAO();
    private CurrencyDAO currencyDao = new CurrencyDAO();
    private ObjectMapper mapper = new ObjectMapper();
    private AddExchangeRateService addService = new AddExchangeRateService();
    private final Validator validator = new Validator();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/"))
        {
            findAllExchangeRates(req, res);
            return;
        }
        if(pathInfo.length() != 7) {
            setMessage(res, 400, "Коды валют пары отсутствуют в адресе");
            return;
        }
        else {
            String baseCurrencyCode = pathInfo.substring(1,4);
            String targetCurrencyCode = pathInfo.substring(4,7);
            if(validator.isValidCurrencyCode(baseCurrencyCode) && validator.isValidCurrencyCode(targetCurrencyCode)) {
                findExchangeRateByCode(req, res, baseCurrencyCode, targetCurrencyCode);
                return;
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{

        res.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        double rate = 0;
        try {
            rate = Double.parseDouble(req.getParameter("rate"));
            if(rate <= 0)
            {
                setMessage(res, 400, "Обменный курс некорректен");
                return;
            }
        } catch (NumberFormatException e) {
            setMessage(res, 400, "Отсутствует нужное поле формы");
            return;
        }
        if(validator.isValidCurrencyCode(baseCurrencyCode) && validator.isValidCurrencyCode(targetCurrencyCode)) {
            try {
                Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
                Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
                if(baseCurrency != null && targetCurrency != null) {
                    ExchangeRate exchangeRate = addService.insertExchangeRate(baseCurrency, targetCurrency, rate);
                    res.getWriter().write(mapper.writeValueAsString(exchangeRate));
                    res.setStatus(201);
                }
                else {
                    setMessage(res, 404, "Одна (или обе) валюта из валютной пары не существует в БД");
                    return;
                }
            } catch (ServiceExeption e) {
                setMessage(res, e.getHttpStatusCode(), e.getMessage());
            } catch (Exception e) {
                setMessage(res, 500, "Внутренняя ошибка сервера");
            }
        }
        else setMessage(res, 400, "Отсутствует нужное поле формы или данные некорректны");
    }

    public void findAllExchangeRates(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            List<ExchangeRate> exchangeRates = ratesDao.getAllExchangeRates();
            res.getWriter().write(mapper.writeValueAsString(exchangeRates));
            res.setStatus(200);
        } catch (SQLException e) {
            setMessage(res, 500, "Внутренняя ошибка сервера");
        }
    }

    public void findExchangeRateByCode(HttpServletRequest req, HttpServletResponse res, String baseCurrencyCode, String targetCurrencyCode) throws IOException{

        try {
            ExchangeRate exchangeRate = ratesDao.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            if(exchangeRate != null) {
                res.getWriter().write(mapper.writeValueAsString(exchangeRate));
                res.setStatus(200);
            }
            else setMessage(res, 404, "Обменный курс для пары не найден");
        } catch (Exception exeption) {
            setMessage(res, 500, "Внутрення ошибка сервера");
        }
    }
    public void setMessage(HttpServletResponse res, int statusCode, String message) throws IOException {

        res.setStatus(statusCode);
        res.setContentType("application/json;charset=UTF-8");

        String json = String.format("{\"message\":\"%s\"}", message);
        res.getWriter().write(json);
    }

}
