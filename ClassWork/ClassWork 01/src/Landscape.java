// Allows reading and writing image files
import javax.imageio.ImageIO;

// Provides basic colors and graphics utilities
import java.awt.*;

// Represents an image as a grid of pixels stored in memory
import java.awt.image.BufferedImage;

// Used for file handling
import java.io.File;

// Handles possible input/output errors
import java.io.IOException;

public class Landscape {
    public static void main(String[] args) {

        // Create an image with a 4:3 aspect ratio
        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);

        // Paint the entire image white to simulate the sky
        for (int x = 0; x < img.getWidth(); x++) {          // Move horizontally across the image
            for (int y = 0; y < img.getHeight(); y++) {     // Move vertically across the image
                img.setRGB(x, y, Color.WHITE.getRGB());     // Set each pixel to white
            }
        }

        // Base vertical position where the grass starts
        int grassBaseHeight = 200;

        // Controls how tall the grass waves are
        int grassAmplitude = 15;

        // Controls how wide the grass waves are
        double grassFrequency = 0.05;

        // Draw the grass using a sine wave
        for (int x = 0; x < img.getWidth(); x++) {          // Go column by column

            // Calculate the wave height for this x position
            int waveY = (int)(grassBaseHeight +
                    grassAmplitude * Math.sin(x * grassFrequency));

            // Fill everything below the wave with green
            for (int y = waveY; y < img.getHeight(); y++) {
                img.setRGB(x, y, Color.GREEN.getRGB());
            }
        }

        // Radius of the sun
        int sunRadius = 45;

        // X coordinate of the sun center
        int sunCenterX = 120;

        // Y coordinate of the sun center
        int sunCenterY = 70;

        // Pre-calculate radius squared to avoid using square roots
        int radiusSquared = sunRadius * sunRadius;

        // Draw the sun as a filled circle
        for (int x = sunCenterX - sunRadius; x <= sunCenterX + sunRadius; x++) {
            for (int y = sunCenterY - sunRadius; y <= sunCenterY + sunRadius; y++) {

                // Make sure we stay inside the image boundaries
                if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
                    continue;
                }

                // Horizontal distance from the center
                int dx = x - sunCenterX;

                // Vertical distance from the center
                int dy = y - sunCenterY;

                // Check if the pixel is inside the circle
                if (dx * dx + dy * dy <= radiusSquared) {
                    img.setRGB(x, y, Color.YELLOW.getRGB());
                }
            }
        }

        // Length of the main rays (up, down, left, right)
        int longRay = 40;

        // Length of the diagonal rays
        int shortRay = 25;

        // Angles where the rays will be drawn
        int[] anglesDeg = {0, 45, 90, 135, 180, 225, 270, 315};

        // Draw each sun ray
        for (int i = 0; i < anglesDeg.length; i++) {

            // Current ray angle in degrees
            int angleDeg = anglesDeg[i];

            // Decide ray length based on angle type
            int rayLength;
            if (angleDeg % 90 == 0) {
                rayLength = longRay;
            } else {
                rayLength = shortRay;
            }

            // Start drawing the ray slightly outside the sun
            int start = sunRadius + 3;

            // Convert the angle to radians for trigonometric functions
            double rad = Math.toRadians(angleDeg);

            // Draw the ray pixel by pixel
            for (int t = start; t <= start + rayLength; t++) {

                // Calculate x position of the ray pixel
                int x = sunCenterX + (int)(t * Math.cos(rad));

                // Calculate y position of the ray pixel
                int y = sunCenterY + (int)(t * Math.sin(rad));

                // Check boundaries before drawing
                if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
                    continue;
                }

                // Paint the ray pixel red
                img.setRGB(x, y, Color.RED.getRGB());
            }
        }

        // Save the final image as a JPG file
        try {
            ImageIO.write(img, "jpg", new File("landscape.jpg"));
        } catch (IOException e) {
            // Print the error if the image cannot be saved
            e.printStackTrace();
        }
    }
}
