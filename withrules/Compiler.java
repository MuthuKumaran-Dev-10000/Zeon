package withrules;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * This class provides a method to compile an Abstract Syntax Tree (AST) into a serialized object.
 */
public class Compiler {
    /**
     * Compiles the given AST into a serialized object and saves it to the specified output file.
     * For a simiplicity the code is serialized (# note This feature need to be fully changed)
     * @param ast The Abstract Syntax Tree to be compiled.
     * @param outputFileName The name of the output file where the compiled AST will be saved.
     */
    public static void compile(Node ast, String outputFileName) {
        try (FileOutputStream fileOut = new FileOutputStream(outputFileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(ast); 
            System.out.println("Compiled successfully: " + outputFileName);
        } catch (Exception e) {
            System.out.println("Error during compilation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
