package withrules;
import java.util.List;

public class ClassNode extends Node {
    private String name;
    private List<VariableNode> properties;
    private FunctionDefinitionNode constructor;
    private List<FunctionDefinitionNode> methods;

    public ClassNode(String name, List<VariableNode> properties, FunctionDefinitionNode constructor, List<FunctionDefinitionNode> methods) {
        this.name = name;
        this.properties = properties;
        this.constructor = constructor;
        this.methods = methods;
    }

    public String getName() { return name; }
    public List<VariableNode> getProperties() { return properties; }
    public FunctionDefinitionNode getConstructor() { return constructor; }
    public List<FunctionDefinitionNode> getMethods() { return methods; }
}
