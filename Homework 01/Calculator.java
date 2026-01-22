public class Calculator {

 
    public static double squareArea(double side) {
        return side * side;
    }

    public static double squarePerimeter(double side) {
        return 4 * side;
    }

    
    public static double rectangleArea(double length, double width) {
        return length * width;
    }

    public static double rectanglePerimeter(double length, double width) {
        return 2 * (length + width);
    }

    
    public static double trianglePerimeter(double a, double b, double c) {
        return a + b + c;
    }

    public static double triangleArea(double a, double b, double c) {
        double s = (a + b + c) / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    
    public static double circleArea(double radius) {
        return Math.PI * radius * radius;
    }

    public static double circlePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }

    
    public static double pentagonArea(double side) {
        return (5 * side * side) / (4 * Math.tan(Math.PI / 5.0));
    }

    public static double pentagonPerimeter(double side) {
        return 5 * side;
    }
}
