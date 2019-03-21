package com.zipcodewilmington.jdbc.tools.database;

import com.mysql.jdbc.Driver;
import com.zipcodewilmington.jdbc.tools.database.connection.ConnectionBuilder;
import com.zipcodewilmington.jdbc.tools.database.connection.ConnectionWrapper;
import com.zipcodewilmington.jdbc.tools.database.connection.StatementExecutor;
import com.zipcodewilmington.jdbc.tools.general.exception.SQLeonError;
import com.zipcodewilmington.jdbc.tools.general.logging.LoggerHandler;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DatabaseInterface {
    default boolean isNull() {
        return getConnectionWrapper().hasDatabase(getName());
    }


    default void drop() {
        String sqlStatement = "DROP DATABASE IF EXISTS %s;";
        getStatementExecutor().execute(sqlStatement, getName());
    }


    default void create() {
        String sqlCreateDatabase = "CREATE DATABASE IF NOT EXISTS %s;";
        getStatementExecutor().execute(sqlCreateDatabase, getName());
    }


    default void use() {
        String sqlStatement = "USE %s;";
        getStatementExecutor().execute(sqlStatement, getName());
    }


    default void disableLogging() {
        LoggerHandler logger = getStatementExecutor().getLogger();
        logger.disablePrinting();
    }


    default void enableLogging() {
        LoggerHandler logger = getStatementExecutor().getLogger();
        logger.enablePrinting();
    }


    default DatabaseTable getTable(String tableName) {
        // todo - replace `null` with `this`
        return new DatabaseTable(null, tableName);
    }


    default <T> void persist(T entity) {
        Class<?> entityClass = entity.getClass();
        boolean isEntity = entityClass.isAnnotationPresent(Entity.class);
        assert (isEntity);

        EntityManager entityManager = getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(entity);

        entityTransaction.commit();
    }


    default StatementExecutor getStatementExecutor() {
        return new StatementExecutor(getConnection());
    }


    default ConnectionWrapper getConnectionWrapper() {
        return new ConnectionWrapper(getConnection());
    }

    Connection getConnection();

    ConnectionBuilder getConnectionBuilder();

    void setConnection(Connection connection);


    EntityManager getEntityManager();


    String getName();

    // Attempt to register JDBC Driver
    static void registerJDBCDriver() {
        Driver driver = null;
        try {
            driver = (Driver) Class.forName(Driver.class.getName()).newInstance();
            DriverManager.registerDriver(driver);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) {
            throw new SQLeonError(e1);
        }
    }
}
