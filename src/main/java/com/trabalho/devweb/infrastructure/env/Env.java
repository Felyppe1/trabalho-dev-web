package com.trabalho.devweb.infrastructure.env;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv = Dotenv.configure()
                                               .ignoreIfMissing()
                                               .load();

    public static String get(String key) {
        String envValue = System.getenv(key);
        if (envValue != null) return envValue;
    
        envValue = dotenv.get(key);
        return envValue;
    }

    public static String get(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (envValue != null) return envValue;
    
        envValue = dotenv.get(key);
        if (envValue != null) return envValue;
    
        return defaultValue;
    }
}