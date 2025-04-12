package com.example.photoshop;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        ComboBox<String> effectComboBox = new ComboBox<>();
        effectComboBox.getItems().addAll("Размытие", "Инверсия", "Чернобелое");

        Button openImageButton = new Button("Выбери изображение");
        Button processButton = new Button("Применить эффект");
        Button saveButton = new Button("Сохранить");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);

        openImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        });

        processButton.setOnAction(event -> {
            Image image = imageView.getImage();
            if (image != null) {
                Image processedImage = null;
                switch (effectComboBox.getValue()) {
                    case "Размытие":
                        processedImage = ProcessService.blur(image);
                        break;
                    case "Инверсия":
                        processedImage = ProcessService.invertColors(image);
                        break;
                    case "Чернобелое":
                        processedImage = ProcessService.grayscale(image);
                        break;
                    default:
                        break;
                }
                if (processedImage != null) {
                    imageView.setImage(processedImage);
                }
            }
        });

        saveButton.setOnAction(event -> {
            Image image = imageView.getImage();
            if (image != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    ProcessService.saveImage(image, file);
                }
            }
        });

        root.getChildren().addAll(openImageButton, effectComboBox, processButton, saveButton, imageView);

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Image Processor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
