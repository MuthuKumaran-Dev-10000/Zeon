
// package withrules;

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.FileInputStream;
// import java.io.ObjectInputStream;
// import java.util.List;
// import java.io.File;

// public class Main {
//     public static void main(String[] args) {
        
//         if (args.length > 2) {
//             System.err.println("Usage: java withrules.Main [-c] <filename>");
//             System.exit(1);
//         }
//         if (args.length > 0 && "--version".equals(args[0])) {
//             System.out.println("Zeon Version: 1.0.0");  
//             return;
//         }
        
//         boolean compileOnly = args.length == 2 && args[0].equals("-c");
//         String fileName = compileOnly ? args[1].trim() : args[0].trim();
//         long startTime = System.nanoTime();
        
//         try {
//             File file = new File(fileName);
//             if (file.exists() && file.isFile()) {
//                 if (fileName.endsWith(".zeon")) {
//                     // System.out.println(fileName);
//                     String code = getCodeFromFile(fileName);
//                     code = processComments(code);
                    
//                     Lexer lexer = new Lexer(code);
//                     List<Token> tokens = lexer.tokenize();
//                     // System.out.println(tokens);
//                     Parser parser = new Parser(tokens);
//                     Node ast = parser.parse();
                    
//                     if (compileOnly) {
//                         String compiledFileName = fileName.replace(".zeon", ".zc");
//                         Compiler.compile(ast, compiledFileName);
//                     }else{
//                         Interpreter interpreter = new Interpreter(ast);
//                         interpreter.interpret();
//                     }
//                 } else if (fileName.endsWith(".zc")) {
//                     Node ast = loadCompiledFile(fileName);
//                     Interpreter interpreter = new Interpreter(ast);
//                     interpreter.interpret();
//                 } else {
//                     System.err.println("Error: Unsupported file extension. Use .zeon or .zc.");
//                     System.exit(1);
//                 }
//             } else {
//                 System.err.println("Error: File not found or invalid.");
//                 System.exit(1);
//             }
//         } catch (Exception e) {
//             System.err.println("Error: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
        
//         long endTime = System.nanoTime(); // End timer
//         double executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
//         System.out.printf("Execution Time : %.6f ms\n", executionTime);
//     }
//     private static String getCodeFromFile(String fileName) {
//         StringBuilder codeBuilder = new StringBuilder();
//         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//             String line;
//             while ((line = br.readLine()) != null) {
//                 codeBuilder.append(line).append("\n");
//             }
//         } catch (Exception e) {
//             System.err.println("Error reading file: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
//         return codeBuilder.toString();
//     }

//     private static String processComments(String code) {
//         StringBuilder processedCode = new StringBuilder();
//         String[] lines = code.split("\n");
//         for (String line : lines) {
//             line = line.trim();
//             if (line.isEmpty()) {
//                 continue; 
//             }

//             int index = line.indexOf("??");
//             if (index != -1) {
//                 line = line.substring(0, index); 
//             }

//             processedCode.append(line).append("\n");
//         }
//         return processedCode.toString();
//     }

//     private static Node loadCompiledFile(String fileName) {
//         try (FileInputStream fileIn = new FileInputStream(fileName);
//              ObjectInputStream in = new ObjectInputStream(fileIn)) {
//             return (Node) in.readObject(); 
//         } catch (Exception e) {
//             System.err.println("Error loading compiled file: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//             return null;
//         }
//     }
// }
package withrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            System.err.println("Usage: zeon [-c] <filename> [-t]");
            System.exit(1);
        }

        if (args.length > 0 && "--version".equals(args[0])) {
            System.out.println("Zeon Version: 1.0.0");
            return;
        }

        boolean compileOnly = args.length >= 2 && args[0].equals("-c");
        boolean showExecutionTime = args.length == 2 && args[1].equals("-t") || 
                                    args.length == 3 && args[2].equals("-t");

        String fileName = compileOnly ? args[1].trim() : args[0].trim();
        long startTime = System.nanoTime();

        // Track memory before execution
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();  // Run garbage collection to get a more accurate memory reading
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        try {
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                System.err.println("Error: File not found or invalid.");
                System.exit(1);
            }
            List<Token> tokens;
            if (fileName.endsWith(".zeon")) {
                String code = getCodeFromFile(fileName);
                code = processComments(code);

                Lexer lexer = new Lexer(code);
                tokens = lexer.tokenize();
                if (showExecutionTime) {
                    System.out.println(tokens);   
                }
                Parser parser = new Parser(tokens);
                Node ast = parser.parse();
                
                if (compileOnly) {
                    Compiler.compile(ast, fileName.replace(".zeon", ".zc"));
                } 
                else {
                    new Interpreter(ast).interpret();
                }

            } else if (fileName.endsWith(".zc")) {
                Node ast = loadCompiledFile(fileName);
                new Interpreter(ast).interpret();
            } else {
                System.err.println("Error: Unsupported file extension. Use .zeon or .zc.");
                System.exit(1);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (showExecutionTime) {
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1_000_000.0;

            // Track memory after execution
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryAfter - memoryBefore;

            System.out.printf("\nExecution Time : %.6f ms\n", executionTime);
            System.out.printf("Memory Used : %.2f KB\n", memoryUsed / 1024.0);
        }
    }

    private static String getCodeFromFile(String fileName) {
        StringBuilder codeBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                codeBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return codeBuilder.toString();
    }

    private static String processComments(String code) {
        StringBuilder processedCode = new StringBuilder();
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            int index = line.indexOf("??");
            if (index != -1) {
                line = line.substring(0, index);
            }

            processedCode.append(line).append("\n");
        }
        return processedCode.toString();
    }

    private static Node loadCompiledFile(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Node) in.readObject();
        } catch (Exception e) {
            System.err.println("Error loading compiled file: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
