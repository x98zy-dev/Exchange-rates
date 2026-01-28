package org.x98zy.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.validation.Validator;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyDAO dao = new CurrencyDAO();
    private ObjectMapper mapper = new ObjectMapper();
    private Validator validator = new Validator();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/"))
        {
           returnAllCurrencies(req, res);
           return;
        }
        String code = pathInfo.substring(1).toUpperCase();
        if (validator.isValidCurrencyCode(code)) {
            returnCurrencyByCode(req, res, code);
        }
        else {
            res.setStatus(400);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String code = req.getParameter("code").toUpperCase();
        String sign = req.getParameter("sign");

        if(name.isBlank() || code.isBlank() || sign.isBlank())
        {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect data");
        }
        else {
            dao.insertCurrency(code, name, sign);
            returnCurrencyByCode(req, res, code);
        }
    }

    public void returnAllCurrencies(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");

        try {
            List<Currency> currencies = dao.getAllCurrencies();

            res.getWriter().write(mapper.writeValueAsString(currencies));
        } catch (Exception e)
        {
            res.setStatus(500);
        }
    }

    public void returnCurrencyByCode(HttpServletRequest req, HttpServletResponse res, String code) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        Currency currency = dao.getCurrencyByCode(code);
        res.getWriter().write(mapper.writeValueAsString(currency));
    }
}
