package com.zipcodewilmington.jdbc.tools.database;

import com.zipcodewilmington.jdbc.tools.collections.ProperStack;
import com.zipcodewilmington.jdbc.tools.database.connection.ResultSetHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DatabaseTableTest {
    private DatabaseTable table;
    private Database database;

    public DatabaseTableTest() {
        this.database = Database.UAT;
        this.database.drop();
        this.database.create();
        this.database.use();
    }

    @Before
    public void setup() {
        new MigrationsTable(database.getConnection()).importFilesFromResources();
        this.table = database.getTable("pokemons");
    }

    @Test
    public void limitTest() {
        // Given
        Integer expectedNumberOfRows = 500;

        // When
        ResultSetHandler results = table.limit(expectedNumberOfRows);
        Integer actualNumberOfRows = results.toStack().size();

        // Then
        Assert.assertEquals(actualNumberOfRows, expectedNumberOfRows);
    }

    @Test
    public void selectTest() {
        // Given
        int expectedNumberOfFields = 2;
        String firstColumn = "name";
        String secondColumn = "secondary_type";
        String columnNames = firstColumn + ", " + secondColumn;

        // When
        ResultSetHandler results = table.select(columnNames);
        Map<String, String> firstRow = results.toStack().pop();
        String firstColumnVal = firstRow.get(firstColumn);
        String secondColumnVal = firstRow.get(secondColumn);
        int actualNumberOfFields = firstRow.size();

        // Then
        Assert.assertEquals(expectedNumberOfFields, actualNumberOfFields);
        Assert.assertNotNull(firstColumnVal);
        Assert.assertNotNull(secondColumnVal);
    }


    @Test
    public void allTest() {
        // Given
        int expectedNumberOfResults = 656;

        // When
        ResultSetHandler results = table.all();
        ProperStack<Map<String, String>> resultsAsStack = results.toStack();
        int actualNumberOfResults = resultsAsStack.size();

        // then
        Assert.assertEquals(expectedNumberOfResults, actualNumberOfResults);


    }

    @Test
    public void toStringTest() {
        System.out.println(table);
    }
}
