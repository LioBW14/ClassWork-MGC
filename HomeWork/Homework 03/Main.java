import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) {

        try {
            // Load the original image from the project folder
            BufferedImage originalImage = ImageIO.read(
                    new File("original.png")
            );

            // Create objects to compress and decompress
            ImageCompressor compressor = new ImageCompressor();
            ImageDecompressor decompressor = new ImageDecompressor();

            // Compress the image into a custom binary file
            compressor.compress(originalImage, "compressed.bin");

            // Decompress the binary file back into an image
            BufferedImage decompressedImage =
                    decompressor.decompress("compressed.bin");

            // Save the reconstructed image as a PNG file
            ImageIO.write(
                    decompressedImage,
                    "png",
                    new File("decompressed.png")
            );

            // Print a success message
            System.out.println("Compression and decompression completed successfully.");

        } catch (Exception e) {
            // Print the error details if something fails
            e.printStackTrace();
        }
    }
}
