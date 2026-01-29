package org.x98zy.currency.exeption;

public class DAOExeption extends RuntimeException {

    private final int httpStatusCode;

    public DAOExeption(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
