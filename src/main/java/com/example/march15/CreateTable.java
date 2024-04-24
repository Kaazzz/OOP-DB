package com.example.march15;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public void createTables() {
        createUsersTable();
        createPagesTable();
    }

    private void createUsersTable() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()){
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL)";

            statement.execute(query);
            System.out.println("Users Table Created Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createPagesTable() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()){
            String query = "CREATE TABLE IF NOT EXISTS pages (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(100) NOT NULL," +
                    "content TEXT," +
                    "user_id INT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";

            statement.execute(query);
            System.out.println("Pages Table Created Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}