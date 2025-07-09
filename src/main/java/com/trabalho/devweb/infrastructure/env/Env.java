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
    
    public static void main(String[] args) {
    System.out.println("DB_URL = " + Env.get("DB_URL"));
    System.out.println("DB_USER = " + Env.get("DB_USER"));
    System.out.println("DB_PASSWORD = " + Env.get("DB_PASSWORD"));
}

}