import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompressor {

    // This method compresses an image using color reduction + RLE
    public void compress(BufferedImage image, String outputFile) throws IOException {

        // Factor used to reduce color precision (lossy compression)
        int compressionFactor = 4;

        // Stream used to write binary data into a file
        DataOutputStream output = new DataOutputStream(
                new FileOutputStream(outputFile)
        );

        // Get image dimensions
        int width = image.getWidth();
        int height = image.getHeight();

        // Write image dimensions at the beginning of the file
        // This will be used during decompression
        output.writeInt(width);
        output.writeInt(height);

        // Variables used for Run-Length Encoding (RLE)
        Color previousColor = null; // Stores the previous pixel color
        int count = 0;              // Counts how many times the color repeats

        // Traverse the image pixel by pixel (row by row)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Read the current pixel color
                Color currentColor = new Color(image.getRGB(x, y));

                // Reduce color precision
                int r = currentColor.getRed() / compressionFactor;
                int g = currentColor.getGreen() / compressionFactor;
                int b = currentColor.getBlue() / compressionFactor;

                // Create a compressed color object
                Color compressedColor = new Color(r, g, b);

                // First pixel case
                if (previousColor == null) {
                    previousColor = compressedColor;
                    count = 1;
                }
                // If the current pixel is the same as the previous one
                // and the count does not exceed 255
                else if (compressedColor.equals(previousColor) && count < 255) {
                    count++;
                }
                // If the color changes or the count reaches its limit
                else {
                    // Write the previous color and its repetition count
                    output.writeByte(previousColor.getRed());
                    output.writeByte(previousColor.getGreen());
                    output.writeByte(previousColor.getBlue());
                    output.writeByte(count);

                    // Reset for the new color
                    previousColor = compressedColor;
                    count = 1;
                }
            }
        }

        // Write the last RLE block to the file
        if (previousColor != null) {
            output.writeByte(previousColor.getRed());
            output.writeByte(previousColor.getGreen());
            output.writeByte(previousColor.getBlue());
            output.writeByte(count);
        }

        // Close the output stream
        output.close();
    }
}
