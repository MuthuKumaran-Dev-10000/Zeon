package withrules;
public class WithNode extends Node {
    private Node fileName;
    private String mode;
    private String variableName;
    private BlockNode body;

    public WithNode(Node fileName, String mode, String variableName, BlockNode body) {
        this.fileName = fileName;
        this.mode = mode;
        this.variableName = variableName;
        this.body = body;
    }

    public Node getFileName() { return fileName; }
    public String getMode() { return mode; }
    public String getVariableName() { return variableName; }
    public BlockNode getBody() { return body; }
}
