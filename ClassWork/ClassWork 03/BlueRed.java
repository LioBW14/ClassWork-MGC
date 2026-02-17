import java.io.FileWriter;
import java.io.IOException;

public class BlueRed {

    public static void main(String[] args) {

        // 4:3 aspect ratio
        int width = 400;
        int height = 300;

        try {

            // Create the SVG file
            FileWriter writer = new FileWriter("BlueRed.svg");

            // Write SVG header with viewBox
            writer.write("<svg width=\"" + width + "\" height=\"" + height + "\" ");
            writer.write("viewBox=\"0 0 " + width + " " + height + "\" ");
            writer.write("xmlns=\"http://www.w3.org/2000/svg\">\n\n");

            // Blue triangle (bottom-left)
            writer.write("    <polygon points=\"0,0 0," + height + " " + width + "," + height + "\" ");
            writer.write("fill=\"blue\" />\n\n");

            // Red triangle (top-right)
            writer.write("    <polygon points=\"0,0 " + width + ",0 " + width + "," + height + "\" ");
            writer.write("fill=\"red\" />\n\n");

            // Close SVG
            writer.write("</svg>");

            writer.close();

            System.out.println("SVG file created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
