package com.GM2.model.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AbstractRepository {

    protected JdbcTemplate jdbcTemplate;
    protected Properties sqlQueries;
    protected String sqlQueriesFileName;

    public void setSqlQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public void createProperties() {
        sqlQueries = new Properties();
        try {
            BufferedReader reader;
            File f = new File(this.sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        } catch (IOException e) {
            System.err.println("Error creating properties object for SQL queries");
            e.printStackTrace();
        }
    }
}
