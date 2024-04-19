module com.example.march15 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.march15 to javafx.fxml;
    exports com.example.march15;
}