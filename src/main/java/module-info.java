module com.example.photoshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.photoshop to javafx.fxml;
    exports com.example.photoshop;
}