import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageDecompressor {

    // This method reconstructs an image from the compressed RLE file
    public BufferedImage decompress(String inputFile) throws IOException {

        // Same compression factor used during compression
        int compressionFactor = 4;

        // Stream used to read binary data from the file
        DataInputStream input = new DataInputStream(
                new FileInputStream(inputFile)
        );

        // Read image dimensions from the file header
        int width = input.readInt();
        int height = input.readInt();

        // Create an empty image where pixels will be restored
        BufferedImage image = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB
        );

        // Coordinates used to place pixels in the image
        int x = 0;
        int y = 0;

        // Read RLE blocks until the image is completely filled
        while (y < height) {

            // Read compressed color and repetition count
            int r = input.readUnsignedByte();
            int g = input.readUnsignedByte();
            int b = input.readUnsignedByte();
            int count = input.readUnsignedByte();

            // Restore color precision
            r = r * compressionFactor;
            g = g * compressionFactor;
            b = b * compressionFactor;

            // Create the restored color
            Color color = new Color(r, g, b);

            // Write the same pixel 'count' times
            for (int i = 0; i < count; i++) {
                image.setRGB(x, y, color.getRGB());
                x++;

                // Move to the next row when reaching image width
                if (x == width) {
                    x = 0;
                    y++;
                }
            }
        }

        // Close the input stream
        input.close();

        return image;
    }
}
