import java.util.*;
public class mk{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
System.out.println("Enter the time $$ || $$ :");
int n = sc.nextInt();
System.out.println("Hello from Java! : "+n);
int fact = 1;
for (int i = 1; i <= n; i++) {
fact *= i;
}
System.out.println("Factorial of " + n + " is: " + fact);
}
}
