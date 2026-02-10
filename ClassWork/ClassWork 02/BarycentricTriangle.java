import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;

public class BarycentricTriangle {

    public static void main(String[] args) {
        try {

            // Image size
            int width = 400;
            int height = 400;

            // Create RGB image
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Triangle vertices
            // A = Red
            double Ax = 50;
            double Ay = 350;

            // B = Green
            double Bx = 350;
            double By = 350;

            // C = Blue
            double Cx = 200;
            double Cy = 50;

            // Barycentric denominator
            double denominator = ((By - Cy) * (Ax - Cx) + (Cx - Bx) * (Ay - Cy));

            // Loop through all pixels
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    // Compute barycentric coordinates
                    double lambda1 = ((By - Cy) * (x - Cx) + (Cx - Bx) * (y - Cy)) / denominator;
                    double lambda2 = ((Cy - Ay) * (x - Cx) + (Ax - Cx) * (y - Cy)) / denominator;
                    double lambda3 = 1 - lambda1 - lambda2;

                    // Check if pixel is inside the triangle
                    if (lambda1 >= 0 && lambda2 >= 0 && lambda3 >= 0) {

                        // Convert barycentric values to RGB
                        int r = (int) (lambda1 * 255);
                        int g = (int) (lambda2 * 255);
                        int b = (int) (lambda3 * 255);

                        // Create color and paint pixel
                        Color color = new Color(r, g, b);
                        image.setRGB(x, y, color.getRGB());

                    } else {
                        // Outside triangle --> black
                        image.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }

            // Save image
            ImageIO.write(image, "png", new File("barycentric_triangle.png"));
            System.out.println("Image created: barycentric_triangle.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
