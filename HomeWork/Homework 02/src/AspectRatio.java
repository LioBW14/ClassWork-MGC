import java.util.Scanner;

public class AspectRatio {
    // This method calculates the GCD (Greatest Common Divisor)
    public static int gcd(int a, int b) {
        while (b != 0) { // Loop until b becomes zero
            int remainder = a % b; // Calculate remainder
            a = b; // Assign b to a
            b = remainder; // Assign remainder to b
        }
        return a; // a is the GCD
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in); // Scanner to read user input

        try {
            System.out.print("Enter width: "); // Ask for width
            int width = sc.nextInt(); // Read width

            System.out.print("Enter height: "); // Ask for height
            int height = sc.nextInt(); // Read height

            // Validate input: width and height must be positive
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException(
                        "Width and height must be positive values. Aspect ratio cannot be negative or zero."
                );
            }

            int divisor = gcd(width, height); // Calculate GCD

            int aspectWidth = width / divisor; // Simplify width
            int aspectHeight = height / divisor; // Simplify height

            System.out.println("Aspect Ratio: " + aspectWidth + ":" + aspectHeight); // Output result

        } catch (IllegalArgumentException e) {
            // Handles negative or zero values
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {
            // Handles invalid input (non-numeric values)
            System.out.println("Error: Please enter valid integer numbers.");

        }
        sc.close();
    }
}
