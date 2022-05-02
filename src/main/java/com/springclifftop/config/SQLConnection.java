package com.springclifftop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class SQLConnection {

    private static final String CLASS_FOR_NAME = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:/Users/crowds/MyTools/SelfTracking/selfTracking.sqlite";


    @Bean(name = "sqlConnection")
    public Connection connectSQL() {
        Connection connection = null;
        try {
            Class.forName(CLASS_FOR_NAME);
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
