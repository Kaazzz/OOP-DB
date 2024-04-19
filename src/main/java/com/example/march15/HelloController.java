package com.example.march15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HelloController {

    public Button btnLogout;
    public VBox pnLogin;
    public PasswordField pfPassword;
    public TextField tfUserName;
    public Button btnSignIn;
    public VBox pnLogout;
    public ColorPicker cpPicker;
    @FXML
    private Label lblAlert;

    public Button btnToggleDarkMode;

    private boolean isDarkMode = false;

    @FXML
    protected void toggleDarkMode() {
        if (isDarkMode) {
            btnToggleDarkMode.setStyle("-fx-background-color: white;");
            btnToggleDarkMode.setText("Toggle Dark Mode");
            btnToggleDarkMode.getScene().getStylesheets().remove(getClass().getResource("user1-darkmode.css").toExternalForm());
            btnToggleDarkMode.getScene().getStylesheets().add(getClass().getResource("user1-lightmode.css").toExternalForm());
        } else {
            btnToggleDarkMode.setStyle("-fx-background-color: black;");
            btnToggleDarkMode.setText("Toggle Light Mode");
            btnToggleDarkMode.getScene().getStylesheets().remove(getClass().getResource("user1-lightmode.css").toExternalForm());
            btnToggleDarkMode.getScene().getStylesheets().add(getClass().getResource("user1-darkmode.css").toExternalForm());
        }
        isDarkMode = !isDarkMode;
    }

    @FXML
    protected void onSignInButtonClick() throws IOException {
        String username = tfUserName.getText();
        String password = pfPassword.getText();

        if (username.equals("admin") && password.equals("admin")) {
            loadHomePage("user1.css", "home-view.fxml");
        } else if (username.equals("admin2") && password.equals("admin2")) {
            loadHomePage("user2.css", "home-view.fxml");
        } else {
            lblAlert.setText("Invalid User Name and/or Password");
        }
    }

    private void loadHomePage(String cssFileName, String fxmlFileName) throws IOException {
        // Load the appropriate CSS file
        AnchorPane p = (AnchorPane) pnLogin.getParent();
        p.getScene().getStylesheets().clear();
        p.getStylesheets().add(getClass().getResource(cssFileName).toExternalForm());

        Parent scene = FXMLLoader.load(getClass().getResource(fxmlFileName));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    @FXML
    protected void onLogoutClick(ActionEvent actionEvent) throws IOException {

        Parent scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));


        VBox parentContainer = (VBox) btnLogout.getParent();

        parentContainer.getScene().getStylesheets().clear();

        parentContainer.getChildren().clear();
        parentContainer.getChildren().add(scene);
    }



}
