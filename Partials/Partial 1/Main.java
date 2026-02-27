import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Load the default image from the project folder
        ImageEditor editor = new ImageEditor("image.png");

        // If the image failed to load, stop the program
        if (!editor.isImageLoaded()) {
            System.out.println("Program terminated. Image not found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Main loop: allows multiple modifications before exiting
        while (true) {

            // Show current image size and options to the user
            System.out.println("\n=================================");
            System.out.println("Current image size: "
                    + editor.getWidth() + " x "
                    + editor.getHeight());
            System.out.println("=================================");

            System.out.println("1. Crop");
            System.out.println("2. Invert");
            System.out.println("3. Rotate");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {

                case 1:
                    //Crop is always based on two coordinates
                    System.out.print("Enter x1 y1 x2 y2: ");
                    editor.crop(scanner.nextInt(), scanner.nextInt(),
                                scanner.nextInt(), scanner.nextInt());
                    break;

                case 2:
                    //Invert affects the selected region
                    System.out.print("Enter x1 y1 x2 y2: ");
                    editor.invertRegion(scanner.nextInt(), scanner.nextInt(),
                                        scanner.nextInt(), scanner.nextInt());
                    break;

                case 3:
                    //Rotate affects the selected region
                    System.out.print("Enter x1 y1 x2 y2: ");
                    int x1 = scanner.nextInt();
                    int y1 = scanner.nextInt();
                    int x2 = scanner.nextInt();
                    int y2 = scanner.nextInt();

                    System.out.print("Enter angle (90, 180, 270): ");
                    int angle = scanner.nextInt();

                    editor.rotateRegion(x1, y1, x2, y2, angle);
                    break;

                case 4:
                    //Automatically save when exiting
                    editor.saveImage("editedimage.png");
                    System.out.println("Image automatically saved as editedimage.png");
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}