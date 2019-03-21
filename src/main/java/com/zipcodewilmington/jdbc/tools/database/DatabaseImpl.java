package com.zipcodewilmington.jdbc.tools.database;

import com.zipcodewilmington.jdbc.tools.database.connection.ConnectionBuilder;
import com.zipcodewilmington.jdbc.tools.database.connection.ConnectionWrapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;

public class DatabaseImpl implements DatabaseInterface {
    private final EntityManager entityManager;
    private final ConnectionBuilder connectionBuilder;
    private final String name;
    private Connection connection;

    public DatabaseImpl(ConnectionBuilder connectionBuilder, String name) {
        Connection connection = connectionBuilder.build();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(name);

        this.name = name;
        this.entityManager = emf.createEntityManager();
        this.connection = connection;
        this.connectionBuilder = connectionBuilder;
    }

    @Override
    public Connection getConnection() {
        ConnectionWrapper connectionWrapper = new ConnectionWrapper(this.connection);
        if (connectionWrapper.isClosed()) {
            setConnection(getConnectionBuilder().build());
        }
        return this.connection;
    }

    @Override
    public ConnectionBuilder getConnectionBuilder() {
        return connectionBuilder;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public String getName() {
        return name;
    }
}
