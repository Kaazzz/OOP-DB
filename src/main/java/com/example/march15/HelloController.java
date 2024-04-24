package com.example.march15;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private TextField tfUserName;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private Label lblAlert;

    @FXML
    private TableView<Page> tvPages;

    @FXML
    private TableColumn<Page, String> colTitle;

    @FXML
    private TableColumn<Page, String> colContent;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfContent;

    private int id;

    public static int loggedId;

    @FXML
    public void initialize() {
        if (colTitle != null && colContent != null) {
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
            loadPages();
        }
    }

    private void loadPages() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM pages WHERE user_id = ?")) {
            statement.setInt(1, loggedId);

            ResultSet rs = statement.executeQuery();
            ObservableList<Page> pages = FXCollections.observableArrayList();
            while (rs.next()) {
                Page page = new Page();
                page.setId(rs.getInt("id"));
                page.setTitle(rs.getString("title"));
                page.setContent(rs.getString("content"));
                page.setUserId(rs.getInt("user_id"));
                pages.add(page);
            }
            tvPages.setItems(pages);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = tfUserName.getText();
        String password = pfPassword.getText();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.createUser();

        lblAlert.setText("Registration Successful. Please Login.");
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));

        AnchorPane parentContainer = (AnchorPane) tfUserName.getScene().getRoot();
        parentContainer.getChildren().clear();
        parentContainer.getChildren().add(scene);
    }

    @FXML
    protected void onSignInButtonClick(ActionEvent event) {
        String username = tfUserName.getText();
        String password = pfPassword.getText();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User existingUser = user.readUserByUsername(username);


        if (existingUser != null && existingUser.getPassword().equals(password)) {
            this.id = existingUser.getId();
            System.out.println("Logged in user ID: " + this.id);
            lblAlert.setText("");
            loggedId = this.id;

            try {
                Parent homePage = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                Scene homeScene = new Scene(homePage);
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(homeScene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblAlert.setText("Invalid username or password.");
        }
    }





    @FXML
    protected void onRegButtonClick(ActionEvent event) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("register-view.fxml"));

        AnchorPane parentContainer = (AnchorPane) tfUserName.getScene().getRoot();
        parentContainer.getChildren().clear();
        parentContainer.getChildren().add(scene);
    }

    @FXML
    protected void onAddButtonClick(ActionEvent event) {
        String title = tfTitle.getText();
        String content = tfContent.getText();
        System.out.println(loggedId);

        if (loggedId != 0) {
            Page newPage = new Page();
            newPage.setTitle(title);
            newPage.setContent(content);
            newPage.setUserId(loggedId);
            newPage.createPage();


            loadPages();
        } else {
            lblAlert.setText("Please log in before adding a page.");
        }
    }



    @FXML
    protected void onDeleteButtonClick(ActionEvent event) {
        Page selectedPage = tvPages.getSelectionModel().getSelectedItem();
        if (selectedPage != null) {
            selectedPage.deletePage();
            loadPages();
        }
    }

    @FXML
    protected void onUpdateButtonClick(ActionEvent event) {
        Page selectedPage = tvPages.getSelectionModel().getSelectedItem();
        if (selectedPage != null) {
            String title = tfTitle.getText();
            String content = tfContent.getText();

            selectedPage.setTitle(title);
            selectedPage.setContent(content);
            selectedPage.updatePage();

            loadPages();
        }
    }


    @FXML
    protected void onDeleteUserButtonClick(ActionEvent event) {
        if (loggedId != 0) {

            Page.deletePagesByUserId(loggedId);


            User user = new User();
            user.setId(loggedId);
            user.deleteUser();


            loggedId = 0;


            try {
                Parent loginPage = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                StackPane root = new StackPane(loginPage);
                Scene loginScene = new Scene(root);

                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(loginScene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            lblAlert.setText("User and associated pages deleted successfully.");
        } else {
            lblAlert.setText("No user logged in.");
        }
    }




}