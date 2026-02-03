// Allows reading and writing image files
import javax.imageio.ImageIO;

// Provides color definitions and basic graphics utilities
import java.awt.*;

// Represents an image stored in memory as pixels
import java.awt.image.BufferedImage;

// Handles input/output exceptions
import java.io.IOException;

// Used to create and manage files
import java.io.File;

public class square {
    public static void main(String[] args) {

        // Creates a 400x300 image in memory using the RGB color model (4:3 aspect ratio)
        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);

        // Iterates through every pixel in the image
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {

                // Defines a diagonal that adapts to the image aspect ratio
                if (x > y * img.getWidth() / img.getHeight()) {
                    // Pixels on one side of the diagonal are colored red
                    img.setRGB(x, y, Color.RED.getRGB());
                } else {
                    // Pixels on the other side of the diagonal are colored blue
                    img.setRGB(x, y, Color.BLUE.getRGB());
                }
            }
        }

        // Writes the generated image to a JPG file
        try {
            ImageIO.write(img, "jpg", new File("square.jpg"));
        } catch (IOException e) {
            // Prints an error message if the file cannot be written
            e.printStackTrace();
        }
    }
}

