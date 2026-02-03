import java.util.Scanner;

public class CoordinateConverter { // Main class

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in); // Scanner to read user input

        try {
            // Display menu
            System.out.println("Coordinate Conversion Menu");
            System.out.println("1) Cartesian to Polar");
            System.out.println("2) Polar to Cartesian");
            System.out.print("Choose an option (1 or 2): ");

            int option = sc.nextInt(); // Read menu option

            switch (option) {

                // Option 1: Cartesian to Polar
                case 1:

                    System.out.print("Enter x coordinate: ");
                    double x = sc.nextDouble(); // Read x value

                    System.out.print("Enter y coordinate: ");
                    double y = sc.nextDouble(); // Read y value

                    // Calculate radius using Pythagorean theorem
                    double r = Math.sqrt(x * x + y * y);

                    // Calculate angle in radians using atan2
                    double thetaRadians = Math.atan2(y, x);

                    // Convert angle from radians to degrees
                    double thetaDegrees = Math.toDegrees(thetaRadians);

                    // Display Polar coordinates
                    System.out.println("Polar Coordinates:");
                    System.out.println("r = " + r);
                    System.out.println("θ = " + thetaDegrees + " degrees");
                    break;

                // Option 2: Polar to Cartesian
                case 2:

                    System.out.print("Enter radius (r): ");
                    double radius = sc.nextDouble(); // Read radius

                    // Validate radius value
                    if (radius < 0) {
                        throw new IllegalArgumentException("Radius cannot be negative.");
                    }

                    System.out.print("Enter angle (theta) in degrees: ");
                    double angleDegrees = sc.nextDouble(); // Read angle

                    // Convert degrees to radians
                    double angleRadians = Math.toRadians(angleDegrees);

                    // Calculate Cartesian coordinates
                    double cartesianX = radius * Math.cos(angleRadians);
                    double cartesianY = radius * Math.sin(angleRadians);

                    // Display Cartesian coordinates
                    System.out.println("Cartesian Coordinates:");
                    System.out.println("x = " + cartesianX);
                    System.out.println("y = " + cartesianY);
                    break;

                // Invalid menu option
                default:
                    System.out.println("Invalid option. Please choose 1 or 2.");
            }

        } catch (IllegalArgumentException e) {
            // Handles logical validation errors
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {
            // Handles invalid input (non-numeric values)
            System.out.println("Error: Please enter valid numeric values.");

        }
        sc.close(); // Close the scanner
    }
}
