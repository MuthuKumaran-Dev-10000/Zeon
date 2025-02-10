import java.util.*;

public class Main {
    public static void fibonacci(int n) {
        int a = 0, b = 1;
        while (n > 0) {
            System.out.print(a + " ");
            int temp = a;
            a = b;
            b = temp + b;
            n--;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // Start timer

        fibonacci(10); // Run Fibonacci

        long endTime = System.nanoTime(); // End timer
        double executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        System.out.printf("Execution Time (Java): %.6f ms\n", executionTime);
    }
}
