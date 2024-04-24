package com.example.march15;

import java.sql.*;

public class Page {
    private int id;
    private String title;
    private String content;
    private int userId;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void createPage() {
        String checkUserSql = "SELECT * FROM users WHERE id = ?";
        String insertPageSql = "INSERT INTO pages (title, content, user_id) VALUES (?, ?, ?)";

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement checkUserStatement = c.prepareStatement(checkUserSql);
             PreparedStatement insertPageStatement = c.prepareStatement(insertPageSql)) {

            checkUserStatement.setInt(1, this.userId);
            ResultSet rs = checkUserStatement.executeQuery();

            if (rs.next()) {
                insertPageStatement.setString(1, this.title);
                insertPageStatement.setString(2, this.content);
                insertPageStatement.setInt(3, this.userId);
                insertPageStatement.executeUpdate();
                System.out.println("Page Created Successfully");
            } else {
                System.out.println("User with id " + this.userId + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Page readPage(int id) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM pages WHERE id = ?")){
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt("id");
                this.title = rs.getString("title");
                this.content = rs.getString("content");
                this.userId = rs.getInt("user_id");
                System.out.println("Page Read Successfully");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void updatePage() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE pages SET title = ?, content = ? WHERE id = ?")){
            statement.setString(1, this.title);
            statement.setString(2, this.content);
            statement.setInt(3, this.id);

            statement.executeUpdate();
            System.out.println("Page Updated Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePage() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE FROM pages WHERE id = ?")){
            statement.setInt(1, this.id);

            statement.executeUpdate();
            System.out.println("Page Deleted Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void deletePagesByUserId(int userId) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE FROM pages WHERE user_id = ?")) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}