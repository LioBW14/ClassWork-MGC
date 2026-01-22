import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Area & Perimeter Calculator");

        boolean running = true;
        while (running) {
            printMenu();
            int option = readInt(sc, "Select an option: ");

            switch (option) {
                case 1:
                    handleSquare(sc);
                    break;
                case 2:
                    handleRectangle(sc);
                    break;
                case 3:
                    handleTriangle(sc);
                    break;
                case 4:
                    handleCircle(sc);
                    break;
                case 5:
                    handleRegularPentagon(sc);
                    break;
                case 0:
                    running = false;
                    System.out.println("The end");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }

            System.out.println();
        }

        sc.close();
    }

    private static void printMenu() {
        System.out.println("1) Square");
        System.out.println("2) Rectangle");
        System.out.println("3) Triangle");
        System.out.println("4) Circle");
        System.out.println("5) Regular Pentagon");
        System.out.println("0) Exit");
    }

    private static void handleSquare(Scanner sc) {
        double side = readPositiveDouble(sc, "Enter side: ");
        double area = Calculator.squareArea(side);
        double perimeter = Calculator.squarePerimeter(side);
        printResult(area, perimeter);
    }

    private static void handleRectangle(Scanner sc) {
        double length = readPositiveDouble(sc, "Enter length: ");
        double width = readPositiveDouble(sc, "Enter width: ");
        double area = Calculator.rectangleArea(length, width);
        double perimeter = Calculator.rectanglePerimeter(length, width);
        printResult(area, perimeter);
    }

    private static void handleTriangle(Scanner sc) {
        double a = readPositiveDouble(sc, "Enter side A: ");
        double b = readPositiveDouble(sc, "Enter side B: ");
        double c = readPositiveDouble(sc, "Enter side C: ");

        
        if (a + b <= c || a + c <= b || b + c <= a) {
            System.out.println("Invalid triangle. The sides do not satisfy the triangle inequality.");
            return;
        }

        double area = Calculator.triangleArea(a, b, c);
        double perimeter = Calculator.trianglePerimeter(a, b, c);
        printResult(area, perimeter);
    }

    private static void handleCircle(Scanner sc) {
        double radius = readPositiveDouble(sc, "Enter radius: ");
        double area = Calculator.circleArea(radius);
        double perimeter = Calculator.circlePerimeter(radius);
        printResult(area, perimeter);
    }

    private static void handleRegularPentagon(Scanner sc) {
        double side = readPositiveDouble(sc, "Enter side: ");
        double area = Calculator.pentagonArea(side);
        double perimeter = Calculator.pentagonPerimeter(side);
        printResult(area, perimeter);
    }

    
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("Value must be greater than 0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void printResult(double area, double perimeter) {
        System.out.println("Area: " + area);
        System.out.println("Perimeter: " + perimeter);
    }
}
