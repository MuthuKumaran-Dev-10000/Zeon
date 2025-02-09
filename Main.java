public class Main {
    public static void greet(String color, int min, int sec) {
        
        System.out.println(color + " Hello, (min: " + min + ", sec: " + sec + ") Zeon!");
        
        // This is a comment
        
        System.out.print("The value of min+sec is: ");
        System.out.println(min + sec);
        
        int i = min;
        int j = sec;
        
        while (min <= sec) {
            while (i <= j) {
                System.out.print(i + " ");
                i++;
            }
            System.out.println();
            min++;
            i = min;
            j = sec;
        }

        
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); // Start time measurement

        greet("_blue hi guys", 1, 5);
        greet("_red", 1, 8);
        long endTime = System.currentTimeMillis(); // End time measurement
        long executionTime = endTime - startTime; // Calculate execution time
        System.out.println("Execution time: " + executionTime + " ms");
    }
}