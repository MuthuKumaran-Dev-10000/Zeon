package withrules;
import java.util.List;

public class FileHandlingNode extends Node {
    private Node fileName;
    private String mode;
    private String fileVariable;
    private List<Node> operations;

    public FileHandlingNode(Node fileName, String mode, String fileVariable, List<Node> operations) {
        this.fileName = fileName;
        this.mode = mode;
        this.fileVariable = fileVariable;
        this.operations = operations;
    }

    public Node getFileName() { return fileName; }
    public String getMode() { return mode; }
    public String getFileVariable() { return fileVariable; }
    public List<Node> getOperations() { return operations; }
}
