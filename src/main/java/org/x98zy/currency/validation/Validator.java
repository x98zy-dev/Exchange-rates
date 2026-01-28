package org.x98zy.currency.validation;

public class Validator {

    public boolean isValidCurrencyCode(String code) {

        return code != null && code.matches("[A-Z]{3}");
    }
}
