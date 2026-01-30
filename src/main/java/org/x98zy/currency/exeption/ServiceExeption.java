package org.x98zy.currency.exeption;

public class ServiceExeption extends Exception {

    private int httpStatusCode;

    public ServiceExeption() {

    }

    public ServiceExeption(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public ServiceExeption(String message, int httpStatusCode, Throwable e) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        Throwable error = e;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }


}
