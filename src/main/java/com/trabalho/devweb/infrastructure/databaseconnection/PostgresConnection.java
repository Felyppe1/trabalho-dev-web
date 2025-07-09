package com.trabalho.devweb.infrastructure.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// import io.github.cdimascio.dotenv.Dotenv;
import com.trabalho.devweb.infrastructure.env.Env;

public class PostgresConnection {
    // private static final Dotenv dotenv = Dotenv.configure()
    //                                             .ignoreIfMissing()
    //                                             .load();
    private static final String URL = Env.get("DB_URL");
    private static final String USER = Env.get("DB_USER");
    private static final String PASSWORD = Env.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do PostgreSQL não encontrado", e);
        }
    }
}