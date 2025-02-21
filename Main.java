import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // Track memory usage before execution
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();  // Trigger garbage collection to get a more accurate reading
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Equivalent of `let y : [1,2,3,4,5];`
        List<Integer> y = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            y.add(i);
        }

        // Equivalent of `print(y);`
        System.out.println(y);

        // Call the `greet` function twice
        greet("_blue hi guys", 1, 5);
        greet("_red", 1, 8);

        // Track memory usage after execution
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        // Calculate execution time
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000.0;

        // Print execution details
        System.out.printf("\nExecution Time: %.6f ms\n", executionTime);
        System.out.printf("Memory Used: %.2f KB\n", memoryUsed / 1024.0);
    }

    // Equivalent of `fn greet(color, min, sec)`
    private static void greet(String color, int min, int sec) {
        System.out.printf("\n%s Hello, (min: %d, sec: %d) Zeon!\n", color, min, sec);
        
        // `print("The value of min+sec is : ");`
        System.out.print("The value of min+sec is : ");
        
        // `print(min + sec);`
        System.out.println(min + sec);

        int i = min;
        int j = sec;

        // Equivalent of the nested while loops
        while (min <= sec) {
            i = min;
            j = sec;
            while (i <= j) {
                System.out.print(i + " ");
                i = i + 1;  // Equivalent of `let i : i + 1;`
            }
            System.out.println();
            min = min + 1;  // Equivalent of `let min : min + 1;`
        }
    }
}
