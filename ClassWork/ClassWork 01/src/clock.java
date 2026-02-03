// Allows reading and writing image files
import javax.imageio.ImageIO;

// Provides color definitions and basic graphics utilities
import java.awt.*;

// Represents an image stored in memory as a grid of pixels
import java.awt.image.BufferedImage;

// Handles input/output related exceptions
import java.io.IOException;

// Used to create and manage files
import java.io.File;

public class clock {
    public static void main(String[] args) {

        // Creates a 400x300 image in memory using the RGB color model (4:3 aspect ratio)
        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);

        // Radius of the main clock circle
        int radius = 100;

        // Calculates the center of the image
        int center_x = img.getWidth() / 2;
        int center_y = img.getHeight() / 2;

        // Fills the entire image with black color (background)
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                img.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        // Draws the main clock circle using sine and cosine
        // Iterates through all angles from 0 to 360 degrees
        for (int angle = 0; angle < 360; angle++) {

            // Converts degrees to radians
            double rad = Math.toRadians(angle);

            // Computes the x and y coordinates of the circle border
            int x = center_x + (int)(radius * Math.cos(rad));
            int y = center_y + (int)(radius * Math.sin(rad));

            // Draws a pixel at the calculated position
            img.setRGB(x, y, Color.WHITE.getRGB());
        }

        // Number of hour markers on a clock
        int hours = 12;

        // Radius at which the hour markers are placed (slightly inside the main circle)
        int hourRadius = radius - 10;

        // Places one pixel for each hour marker using angular positions
        for (int i = 0; i < hours; i++) {

            // Each hour corresponds to a 30 degree step (360 / 12)
            double angle = Math.toRadians(i * 30);

            // Calculates the position of each hour marker
            int x = center_x + (int)(hourRadius * Math.cos(angle));
            int y = center_y + (int)(hourRadius * Math.sin(angle));

            // Draws a single pixel to represent the hour mark
            img.setRGB(x, y, Color.WHITE.getRGB());
        }

        // Defines the minute value (used to control the minute hand position)
        int minute = 120;

        // Length of the minute hand
        int minuteLength = 80;

        // Converts the minute value into an angle and adjusts it so 0 minutes points upward
        double minuteAngle = Math.toRadians(minute * 6 - 90);

        // Draws the minute hand as a radial line from the center
        for (int i = 0; i < minuteLength; i++) {
            int x = center_x + (int)(i * Math.cos(minuteAngle));
            int y = center_y + (int)(i * Math.sin(minuteAngle));

            img.setRGB(x, y, Color.WHITE.getRGB());
        }

        // Defines the hour value
        int hour = 3;

        // Length of the hour hand (shorter than the minute hand)
        int hourLength = 50;

        // Calculates the hour hand angle, including minute influence for smooth movement
        double hourAngle = Math.toRadians((hour % 12) * 30 + minute * 0.5 - 90);

        // Draws the hour hand as a radial line from the center
        for (int i = 0; i < hourLength; i++) {
            int x = center_x + (int)(i * Math.cos(hourAngle));
            int y = center_y + (int)(i * Math.sin(hourAngle));

            img.setRGB(x, y, Color.WHITE.getRGB());
        }

        // Writes the generated image to a JPG file
        try {
            ImageIO.write(img, "jpg", new File("clock.jpg"));
        } catch (IOException e) {
            // Prints an error message if the image file cannot be written
            e.printStackTrace();
        }
    }
}
