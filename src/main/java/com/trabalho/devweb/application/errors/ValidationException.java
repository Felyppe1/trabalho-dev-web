package com.trabalho.devweb.application.errors;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final List<Map<String, String>> errors;

    public ValidationException(List<Map<String, String>> errors) {
        super("Erro(s) de validação encontrados.");
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}