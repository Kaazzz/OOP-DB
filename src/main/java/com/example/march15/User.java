package com.example.march15;

import java.sql.*;

public class User {
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void createUser() {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setString(1, this.username);
            statement.setString(2, this.password);
            statement.executeUpdate();
            System.out.println("User Created Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User readUser(int id) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM users WHERE id = ?")){
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt("id");
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                System.out.println("User Read Successfully");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public User readUserByUsername(String username) {
        User user = null;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM users WHERE username = ?")){
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                System.out.println("User Read Successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }


    public void updateUser() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE users SET username = ?, password = ? WHERE id = ?")){
            statement.setString(1, this.username);
            statement.setString(2, this.password);
            statement.setInt(3, this.id);

            statement.executeUpdate();
            System.out.println("User Updated Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser() {
        try (Connection connection = MySQLConnection.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.id);
                statement.executeUpdate();
                System.out.println("User Deleted Successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user from database", e);
        }
    }
}