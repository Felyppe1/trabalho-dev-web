package com.trabalho.devweb.application.errors;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Erro(s) de validação encontrados.");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}