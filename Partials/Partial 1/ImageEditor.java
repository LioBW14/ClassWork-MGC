import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageEditor {

    //This stores the current working image in memory.
    //Every modification updates this same variable.
    private BufferedImage currentImage;

    //Constructor loads the image from the project folder.
    public ImageEditor(String fileName) {
        try {
            File file = new File(fileName);
            currentImage = ImageIO.read(file);
            System.out.println("Image loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            currentImage = null;
        }
    }

    //Check if the image loaded correctly.
    public boolean isImageLoaded() {
        return currentImage != null;
    }

    public int getWidth() {
        return currentImage.getWidth();
    }

    public int getHeight() {
        return currentImage.getHeight();
    }

    //Save the current image to a new file.
    public void saveImage(String outputFileName) {
        try {
            File outputFile = new File(outputFileName);
            ImageIO.write(currentImage, "png", outputFile);
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }

    //Helper method to calculate rectangle boundaries from two coordinates.
    //This avoids repeating Math.min and Math.abs inside crop and rotate methods.

    private int[] getRectangleData(int x1, int y1, int x2, int y2) {

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);

        return new int[]{minX, minY, width, height};
    }

    //Makes sure the selected rectangle stays inside the image.
    private boolean isValidRectangle(int minX, int minY, int width, int height) {

        return !(minX < 0 || minY < 0 || minX + width > currentImage.getWidth() || minY + height > currentImage.getHeight() || width == 0 || height == 0);
    }

    //Crop the image using two coordinates.
    public void crop(int x1, int y1, int x2, int y2) {

        int[] rect = getRectangleData(x1, y1, x2, y2);
        int minX = rect[0];
        int minY = rect[1];
        int width = rect[2];
        int height = rect[3];

        if (!isValidRectangle(minX, minY, width, height)) {
            System.out.println("Invalid crop coordinates.");
            return;
        }

        //TYPE_INT_ARGB prevents transparency issues
        BufferedImage cropped =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //Copy pixels one by one into the new image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cropped.setRGB(x, y,
                        currentImage.getRGB(minX + x, minY + y));
            }
        }

        currentImage = cropped;

        System.out.println("Crop applied successfully.");
    }

    //Invert the entire image.
    public void invert() {

        int width = currentImage.getWidth();
        int height = currentImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int argb = currentImage.getRGB(x, y);

                //Extract alpha and RGB components
                int alpha = (argb >> 24) & 0xFF;
                int red   = (argb >> 16) & 0xFF;
                int green = (argb >> 8)  & 0xFF;
                int blue  = argb & 0xFF;

                //Invert RGB values
                red   = 255 - red;
                green = 255 - green;
                blue  = 255 - blue;

                //Rebuild pixel while keeping original alpha
                int invertedARGB = (alpha << 24) |
                                   (red << 16) |
                                   (green << 8) |
                                   blue;

                currentImage.setRGB(x, y, invertedARGB);
            }
        }

        System.out.println("Entire image inverted successfully.");
    }

    //Rotate a selected region by 90, 180 or 270 degrees.
    public void rotateRegion(int x1, int y1, int x2, int y2, int angle) {

        if (angle != 90 && angle != 180 && angle != 270) {
            System.out.println("Invalid rotation angle.");
            return;
        }

        int[] rect = getRectangleData(x1, y1, x2, y2);
        int minX = rect[0];
        int minY = rect[1];
        int width = rect[2];
        int height = rect[3];

        if (!isValidRectangle(minX, minY, width, height)) {
            System.out.println("Invalid rotation coordinates.");
            return;
        }

        //Extract selected region
        BufferedImage region = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                region.setRGB(x, y, currentImage.getRGB(minX + x, minY + y));
            }
        }

        BufferedImage rotated;

        //Swap dimensions if needed
        if (angle == 90 || angle == 270)
            rotated = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
        else
            rotated = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //Apply rotation formula
        for (int y = 0; y < region.getHeight(); y++) {
            for (int x = 0; x < region.getWidth(); x++) {

                int pixel = region.getRGB(x, y);

                if (angle == 90)
                    rotated.setRGB(region.getHeight() - 1 - y, x, pixel);
                else if (angle == 180)
                    rotated.setRGB(region.getWidth() - 1 - x,
                                   region.getHeight() - 1 - y, pixel);
                else
                    rotated.setRGB(y,
                                   region.getWidth() - 1 - x, pixel);
            }
        }

        //Paste rotated region back into original image
        for (int y = 0; y < rotated.getHeight(); y++) {
            for (int x = 0; x < rotated.getWidth(); x++) {

                if (minX + x < currentImage.getWidth() && minY + y < currentImage.getHeight()) {
                    currentImage.setRGB(minX + x, minY + y, rotated.getRGB(x, y));
                }
            }
        }

        System.out.println("Region rotated successfully.");
    }
}