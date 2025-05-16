package com.trabalho.devweb.application.errors;

import java.util.Map;

public class BusinessRuleException extends Exception {
    private final Map<String, String> errors;

    public BusinessRuleException(Map<String, String> errors) {
        super("Erros de validação.");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
