package com.zipcodewilmington.jdbc.tools.database;

import com.zipcodewilmington.jdbc.tools.database.connection.ConnectionBuilder;

import javax.persistence.EntityManager;
import java.sql.Connection;

public enum Database implements DatabaseInterface{
    POKEMON(new ConnectionBuilder()
            .setUrl("jdbc:mysql://localhost/")
            .setPort(3306)
            .setDatabaseName("pokemon")
            .setServerName("127.0.0.1")
            .setUser("root")
            .setPassword("newpass")
            .setServerTimezone("UTC")),

    UAT(new ConnectionBuilder()
            .setUrl("jdbc:mysql://localhost/")
            .setPort(3306)
            .setDatabaseName("uat")
            .setServerName("127.0.0.1")
            .setUser("root")
            .setPassword("newpass")
            .setServerTimezone("UTC"));

    static { // Attempt to register JDBC Driver
        DatabaseInterface.registerJDBCDriver();
    }

    private final DatabaseImpl database;

    Database(ConnectionBuilder connectionBuilder) {
        this.database = new DatabaseImpl(connectionBuilder, name());
    }

    @Override
    public Connection getConnection() {
        return database.getConnection();
    }

    @Override
    public ConnectionBuilder getConnectionBuilder() {
        return database.getConnectionBuilder();
    }

    @Override
    public void setConnection(Connection connection) {
        database.setConnection(connection);
    }

    @Override
    public EntityManager getEntityManager() {
        return database.getEntityManager();
    }

    @Override
    public String getName() {
        return database.getName();
    }
}
