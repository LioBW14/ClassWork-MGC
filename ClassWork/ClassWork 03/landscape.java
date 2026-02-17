import java.io.FileWriter;
import java.io.IOException;

public class landscape {

    public static void main(String[] args) {

        // 4:3 aspect ratio
        int width = 400;
        int height = 300;

        try {

            // Create SVG file
            FileWriter writer = new FileWriter("landscape.svg");

            // SVG header
            writer.write("<svg width=\"" + width + "\" height=\"" + height + "\" ");
            writer.write("viewBox=\"0 0 " + width + " " + height + "\" ");
            writer.write("xmlns=\"http://www.w3.org/2000/svg\">\n\n");

            // Sky background
            writer.write("    <rect width=\"" + width + "\" height=\"" + height + "\" fill=\"white\" />\n\n");

            // Sun rays (draw first so sun covers the center)
            writer.write("    <line x1=\"80\" y1=\"10\" x2=\"80\" y2=\"150\" stroke=\"red\" stroke-width=\"2\" />\n");
            writer.write("    <line x1=\"10\" y1=\"80\" x2=\"150\" y2=\"80\" stroke=\"red\" stroke-width=\"2\" />\n");

            // 45 degree rays
            writer.write("    <line x1=\"30\" y1=\"30\" x2=\"130\" y2=\"130\" stroke=\"red\" stroke-width=\"2\" />\n");
            writer.write("    <line x1=\"130\" y1=\"30\" x2=\"30\" y2=\"130\" stroke=\"red\" stroke-width=\"2\" />\n\n");

            // Sun (draw after rays so it overlaps center)
            writer.write("    <circle cx=\"80\" cy=\"80\" r=\"40\" fill=\"yellow\" />\n\n");

            // Grass with 6 hills
            writer.write("    <path d=\"M 0 200 ");
            writer.write("Q 30 160 60 200 ");
            writer.write("Q 90 240 120 200 ");
            writer.write("Q 150 160 180 200 ");
            writer.write("Q 210 240 240 200 ");
            writer.write("Q 270 160 300 200 ");
            writer.write("Q 330 240 360 200 ");
            writer.write("Q 390 160 400 200 ");
            writer.write("L 400 300 L 0 300 Z\" ");
            writer.write("fill=\"lime\" />\n\n");

            // Close SVG
            writer.write("</svg>");

            writer.close();

            System.out.println("Landscape SVG created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
