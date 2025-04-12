package com.example.photoshop;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProcessService {


    public static Image grayscale(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color grayColor = new Color(avg, avg, avg, 1.0);
                writer.setColor(x, y, grayColor);
            }
        }
        return writableImage;
    }


    public static Image invertColors(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                Color invertedColor = color.invert();
                writer.setColor(x, y, invertedColor);
            }
        }
        return writableImage;
    }

    // Размытие изображения (простое усреднение соседних пикселей)
    public static Image blur(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter writer = writableImage.getPixelWriter();

        int kernelSize = 3;
        int offset = kernelSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0;
                int count = 0;


                for (int ky = -offset; ky <= offset; ky++) {
                    for (int kx = -offset; kx <= offset; kx++) {
                        int newX = Math.min(Math.max(x + kx, 0), width - 1);
                        int newY = Math.min(Math.max(y + ky, 0), height - 1);

                        Color color = reader.getColor(newX, newY);
                        r += color.getRed();
                        g += color.getGreen();
                        b += color.getBlue();
                        count++;
                    }
                }


                r /= count;
                g /= count;
                b /= count;

                Color blurredColor = new Color(r, g, b, 1.0);
                writer.setColor(x, y, blurredColor);
            }
        }
        return writableImage;
    }


    public static void saveImage(Image image, File file) {
        try {

            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            PixelReader reader = image.getPixelReader();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    int rgba = (int) (color.getRed() * 255) << 16 | (int) (color.getGreen() * 255) << 8 | (int) (color.getBlue() * 255) | (int) (color.getOpacity() * 255) << 24;
                    bufferedImage.setRGB(x, y, rgba);
                }
            }


            ImageIO.write(bufferedImage, "PNG", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
