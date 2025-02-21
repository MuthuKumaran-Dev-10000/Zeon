package withrules;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
interface Nodee{}
abstract class Node implements Serializable{}

class BlockNode extends Node {
    private List<Node> statements;

    public BlockNode(List<Node> statements) {
        this.statements = statements;
    }

    public List<Node> getStatements() {
        return statements;
    }
}
class FunctionDefinitionNode extends Node {
    private String functionName;
    private List<String> parameters;
    private BlockNode body;

    public FunctionDefinitionNode(String functionName, List<String> parameters, BlockNode body) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.body = body;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "FunctionDefinitionNode{" + "functionName='" + functionName + '\'' + ", parameters=" + parameters + ", body=" + body + '}';
    }
}
class FunctionCallNode extends Node {
    private String functionName;
    private List<Node> arguments;
    private String objectName;
    public FunctionCallNode(String functionName, List<Node> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
        this.objectName = null;
    }
    public FunctionCallNode(String objectName, String functionName, List<Node> arguments) {
        this.objectName = objectName;
        this.functionName = functionName;
        this.arguments = arguments;
    }
    public String getObject() {  // This method was missing!
        return objectName;
    }
    public String getFunctionName() {
        return functionName;
    }

    public List<Node> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "FunctionCallNode{" + "functionName='" + functionName + '\'' + ", arguments=" + arguments + '}';
    }
}

class VariableDeclarationNode extends Node {
    private String varName;
    private Node value;

    public VariableDeclarationNode(String varName, Node value) {
        this.varName = varName;
        this.value = value;
    }

    public String getVarName() {
        return varName;
    }

    public Node getValue() {
        return value;
    }
}
class FloatNode extends Node {
    private final double value;

    public FloatNode(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatNode{" + "value=" + value + '}';
    }
}

class ListNode extends Node {
    private List<Node> elements;

    public ListNode(List<Node> elements) {
        this.elements = elements;
    }

    public List<Node> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "ListNode{" + "elements=" + elements + '}';
    }
}
class TupleNode extends Node {
    private List<Node> elements;

    public TupleNode(List<Node> elements) {
        this.elements = elements;
    }

    public List<Node> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "TupleNode{" + "elements=" + elements + '}';
    }
}
class TypeNode extends Node {
    private Node value;

    public TypeNode(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TypeNode{" + "value=" + value + '}';
    }
}
class TypeCastNode extends Node {
    private TokenType castType;
    private Node value;

    public TypeCastNode(TokenType castType, Node value) {
        this.castType = castType;
        this.value = value;
    }

    public TokenType getCastType() {
        return castType;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TypeCastNode{" + "castType=" + castType + ", value=" + value + '}';
    }
}
class CodeBlockNode extends Node {
    private TokenType blockType;
    private String code;
    private String sendVariable;
    private final Node context;

    public CodeBlockNode(TokenType blockType, String code, String sendVariable) {
        this.blockType = blockType;
        this.code = code;
        this.sendVariable = sendVariable;
        this.context = null;
    }
    
    public TokenType getBlockType() {
        return blockType;
    }

    public String getCode() {
        return code;
    }

    public String getSendVariable() {
        return sendVariable;
    }

    @Override
    public String toString() {
        return "CodeBlockNode{" + "blockType=" + blockType + ", code='" + code + "', sendVariable='" + sendVariable + "'}";
    }
}

class DictNode extends Node {
    private Map<Node, Node> pairs;

    public DictNode(Map<Node, Node> pairs) {
        this.pairs = pairs;
    }

    public Map<Node, Node> getPairs() {
        return pairs;
    }

    @Override
    public String toString() {
        return "DictNode{" + "pairs=" + pairs + '}';
    }
}
 class ForLoopNode extends Node {
    private VariableDeclarationNode initialization;
    private Node condition;
    private VariableDeclarationNode increment;
    private BlockNode body;

    public ForLoopNode(VariableDeclarationNode initialization, Node condition, VariableDeclarationNode increment, BlockNode body) {
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public VariableDeclarationNode getInitialization() {
        return initialization;
    }

    public Node getCondition() {
        return condition;
    }

    public VariableDeclarationNode getIncrement() {
        return increment;
    }

    public BlockNode getBody() {
        return body;
    }
}

class IfElseNode extends Node {
    private Node condition;  
    private BlockNode ifBody;  
    private ElseIfNode elseIfNode;  
    private BlockNode elseBody;  

    
    public IfElseNode(Node condition, BlockNode ifBody, ElseIfNode elseIfNode, BlockNode elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseIfNode = elseIfNode;
        this.elseBody = elseBody;
    }

    
    public Node getCondition() {
        return condition;
    }

    public BlockNode getIfBody() {
        return ifBody;
    }

    public ElseIfNode getElseIfNode() {
        return elseIfNode;
    }

    public BlockNode getElseBody() {
        return elseBody;
    }
}


class BooleanNode extends Node {
    private boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}


class PrintNode extends Node {
    private Node value;

    public PrintNode(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }
}

public class UnaryOperationNode extends Node {
    private String operator; 
    private Node operand;    

    public UnaryOperationNode(String operator, Node operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public Node getOperand() {
        return operand;
    }

    @Override
    public String toString() {
        return "UnaryOperationNode{" +
                "operator='" + operator + '\'' +
                ", operand=" + operand +
                '}';
    }
}


// class ReturnNode extends Node {
//     private Node value;

//     public ReturnNode(Node value) {
//         this.value = value;
//     }

//     public Node getValue() {
//         return value;
//     }
//     public Node getExpression() {
//         return value;
//     }

//     @Override
//     public String toString() {
//         return "ReturnNode{" +
//                 "expression=" + value +
//                 '}';
//     }
// }

















class SwitchNode extends Node {
    private Node switchExpression;
    private List<CaseNode> cases;
    private Node defaultCase;

    public SwitchNode(Node switchExpression, List<CaseNode> cases, Node defaultCase) {
        this.switchExpression = switchExpression;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    public Node getSwitchExpression() { return switchExpression; }
    public List<CaseNode> getCases() { return cases; }
    public Node getDefaultCase() { return defaultCase; }
}

class CaseNode extends Node {
    private Node caseValue;
    private Node caseBody;

    public CaseNode(Node caseValue, Node caseBody) {
        this.caseValue = caseValue;
        this.caseBody = caseBody;
    }

    public Node getCaseValue() { return caseValue; }
    public Node getCaseBody() { return caseBody; }
}














class NumberNode extends Node {
    private int value;

    public NumberNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

class StringNode extends Node {
    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class VariableNode extends Node {
    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class BinaryOperationNode extends Node {
    private Node left;
    private String operator;
    private Node right;

    public BinaryOperationNode(Node left, String operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public Node getRight() {
        return right;
    }
}

class InputNode extends Node {
    private Node prompt;

    public InputNode(Node prompt) {
        this.prompt = prompt;
    }

    public Node getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "InputNode{prompt=" + prompt + "}";
    }
}

class WhileNode extends Node {
    private Node condition;
    private BlockNode body;

    public WhileNode(Node condition, BlockNode body) {
        this.condition = condition;
        this.body = body;
    }

    public Node getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileNode{condition=" + condition + ", body=" + body + "}";
    }
}
