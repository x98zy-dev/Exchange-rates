package org.x98zy.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.x98zy.currency.dao.CurrencyDAO;
import org.x98zy.currency.entity.Currency;
import org.x98zy.currency.exeption.DAOExeption;
import org.x98zy.currency.validation.Validator;

import java.io.IOException;
import java.sql.SQLException;
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
            findAllCurrencies(req, res);
            return;
        }
        String code = pathInfo.substring(1).toUpperCase();
        if (validator.isValidCurrencyCode(code)) {
            findCurrencyByCode(req, res, code);
        }
        else {
            setMessage(res,400, "Код валюты отсутствует в адресе");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");

        String name = req.getParameter("name");
        String code = req.getParameter("code").toUpperCase();
        String sign = req.getParameter("sign");

        if(name == null || code == null ||sign == null) {
            setMessage(res, 400, "Отсутствует нужное поле формы");
            return;
        }
        if(name.isBlank() || code.isBlank() || sign.isBlank())
        {
            setMessage(res, 400, "Отсутствует нужное поле формы");
            return;
        }
        else {
            try {
                dao.insertCurrency(code, name, sign);
                findCurrencyByCode(req, res, code);
                res.setStatus(201);
            } catch (SQLException e) {
                if("23505".equals(e.getSQLState()))
                    setMessage(res, 409, "Валюта с таким кодом уже существует");
                else setMessage(res, 500, "Внутренняя ошибка сервера");
            } catch(Exception exception)
            {
                setMessage(res, 500, "Внутренняя ошибка сервера");
            }
        }
    }

    public void findAllCurrencies(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json;charset=UTF-8");

        try {
            List<Currency> currencies = dao.getAllCurrencies();
            res.getWriter().write(mapper.writeValueAsString(currencies));
            res.setStatus(200);
        } catch (SQLException e)
        {
            setMessage(res,500, "Внутренняя ошибка сервера");
        }
    }

    public void findCurrencyByCode(HttpServletRequest req, HttpServletResponse res, String code) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");

        try {
            Currency currency = dao.getCurrencyByCode(code);
            if(currency != null) {
                res.getWriter().write(mapper.writeValueAsString(currency));
                res.setStatus(200);
            } else {
                setMessage(res, 404, "Валюта не найдена");
            }
        } catch (SQLException e)
        {
            setMessage(res, 500, "Внутрення ошибка сервера");
        }
    }

    public void setMessage (HttpServletResponse res, int statusCode, String message) throws IOException {

        res.setStatus(statusCode);
        res.setContentType("application/json;charset=UTF-8");

        String json = String.format("{\"message\":\"%s\"}", message);
        res.getWriter().write(json);
    }

}
