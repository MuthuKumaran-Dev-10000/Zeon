package withrules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    private final Node ast;
    private Map<String, Object> variables = new HashMap<>();
        private final Map<String, FunctionDefinitionNode> functions = new HashMap<>();
        private final Map<String, String> stringVariables = new HashMap<>();
    
        public Interpreter(Node ast) {
            this.ast = ast;
        }
    
        public Object interpret() {
            return interpretNode(ast);
        }
    
        private Object interpretIfElseNode(IfElseNode node) {
            
            Object condition = interpretNode(node.getCondition());
            
            if (!(condition instanceof Boolean)) {
                throw new RuntimeException("Condition must evaluate to a Boolean, but found: " + condition.getClass());
            }
            if ((boolean) condition) {
                return interpretBlockNode(node.getIfBody());
            } 
            ElseIfNode elseIfNode = node.getElseIfNode();  
        while (elseIfNode != null) {
            
            condition = interpretNode(elseIfNode.getCondition());
            
            
            if (condition instanceof Boolean && (boolean) condition) {
                return interpretBlockNode(elseIfNode.getIfBody());
            }
            
            
            elseIfNode = elseIfNode.getNextElseIfNode();
        }
        
            if (node.getElseBody() != null) {
                return interpretBlockNode(node.getElseBody());
            }
            return null;
        }
        private Object interpretInputNode(InputNode node) {
        Object promptValue = interpretNode(node.getPrompt());
        
        
        System.out.print(promptValue + " "); 
        
        
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
    
        
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            return userInput;
        }
    }
    private Object interpretWhileNode(WhileNode node) {
        while (true) {
            Object conditionValue = interpretNode(node.getCondition());
    
            
            if (!(conditionValue instanceof Boolean)) {
                throw new RuntimeException("While condition must be Boolean, found: " + conditionValue.getClass());
            }
    
            if (!(Boolean) conditionValue) {
                break; 
            }
    
            interpretBlockNode(node.getBody()); 
        }
    
        return null;
    }
    
    private Object interpretListNode(ListNode node) {
    List<Object> interpretedElements = new ArrayList<>();
    for (Node element : node.getElements()) {
        interpretedElements.add(interpretNode(element));
    }
    return interpretedElements;
}
private Object interpretTupleNode(TupleNode node) {
    List<Object> interpretedElements = new ArrayList<>();
    for (Node element : node.getElements()) {
        interpretedElements.add(interpretNode(element));
    }
    return Collections.unmodifiableList(interpretedElements); 
}
private Object interpretTypeNode(TypeNode node) {
    Object value = interpretNode(node.getValue());
    
    if (value instanceof Integer) {
        System.out.println("number : "+value);
    } else if (value instanceof String) {
        System.out.println("string : "+value);
    } else if (value instanceof Boolean) {
        System.out.println("boolean : "+value);
    } else if (value instanceof Double) {
        System.out.println("float : "+value);
    }else if (value instanceof List) {
        System.out.println("list : "+value);
    } else if (value instanceof Map) {
        System.out.println("dictionary : "+value);
    } else {
        System.out.println("unknown : "+value);
    }
    
    return null; 
}
private Object interpretTypeCastNode(TypeCastNode node) {
    Object value = interpretNode(node.getValue());

    switch (node.getCastType()) {
        case CAST_LIST:
            if (value instanceof Integer) {
                return Collections.singletonList(value);
            } else if (value instanceof String) {
                return Arrays.asList(((String) value).split(""));
            } else if (value instanceof List) {
                return value;
            } else {
                throw new RuntimeException("Cannot convert to list: " + value);
            }

        case CAST_STRING:
            return value.toString();

        case CAST_NUMBER:
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else if (value instanceof Float) {
                return ((Float) value).intValue();
            } else {
                return value;
            }

        case CAST_FLOAT:
            if (value instanceof String) {
                return Float.parseFloat((String) value);
            } else if (value instanceof Integer) {
                return (float) (int) value;
            } else {
                return value;
            }

        default:
            throw new RuntimeException("Unknown type cast: " + node.getCastType());
    }
}

private Object interpretDictNode(DictNode node) {
    Map<Object, Object> interpretedPairs = new HashMap<>();
    for (Map.Entry<Node, Node> entry : node.getPairs().entrySet()) {
        Object key = interpretNode(entry.getKey());
        Object value = interpretNode(entry.getValue());
        interpretedPairs.put(key, value);
    }
    return interpretedPairs;
}
private Object executeJava(String javaCode) {
    try {
        String className = "ZeonJavaRunner";
        String javaFileName = className + ".java";

        
        StringBuilder imports = new StringBuilder();
        StringBuilder mainCode = new StringBuilder();

        for (String line : javaCode.split("\n")) {
            if (line.trim().startsWith("import ")) {
                imports.append(line).append("\n"); 
            } else {
                mainCode.append(line).append("\n"); 
            }
        }

        
        String javaProgram = imports.toString() + "\n" + 
                             "public class " + className + " { public static void main(String[] args) throws Exception {\n" + 
                             mainCode.toString() + "\n} }";

        
        Files.write(Paths.get(javaFileName), javaProgram.getBytes());

        
        Process compile = new ProcessBuilder("javac", javaFileName).inheritIO().start();
        compile.waitFor(); 

        
        Process run = new ProcessBuilder("java", className).inheritIO().start();
        run.waitFor(); 

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
private Object executePython(String pythonCode) {
    try {
        String pythonFileName = "zeon_python_script.py";

        
        Files.write(Paths.get(pythonFileName), pythonCode.getBytes());

        
        Process process = new ProcessBuilder("python", pythonFileName).inheritIO().start();
        process.waitFor(); 

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

private Object interpretCodeBlockNode(CodeBlockNode node) {
    if (node.getBlockType() == TokenType.JAVA_BLOCK) {
        return executeJava(node.getCode());
    } else if (node.getBlockType() == TokenType.PYTHON_BLOCK) {
        return executePython(node.getCode());
    }

    throw new RuntimeException("Unknown code block type: " + node.getBlockType());
}



        private Object interpretNode(Node node) {
            if (node instanceof BlockNode) {
                return interpretBlockNode((BlockNode) node);
            }
            if (node instanceof FloatNode) {
                return ((FloatNode) node).getValue();
            } 
            if (node instanceof TypeNode) {
                return interpretTypeNode((TypeNode) node);
            }
            if (node instanceof TypeCastNode) {
                return interpretTypeCastNode((TypeCastNode) node);
            }
            if (node instanceof CodeBlockNode) {
    return interpretCodeBlockNode((CodeBlockNode) node);
}

            
            
            if (node instanceof ListNode) {
                return interpretListNode((ListNode) node);
            }
            if (node instanceof TupleNode) {
                return interpretTupleNode((TupleNode) node);
            }
            if (node instanceof DictNode) {
                return interpretDictNode((DictNode) node);
            }
            if (node instanceof InputNode) {
                return interpretInputNode((InputNode) node);
            }
            if (node instanceof IfElseNode) {
                return interpretIfElseNode((IfElseNode) node);
            } 
            if (node instanceof FunctionDefinitionNode) {
                return interpretFunctionDefinitionNode((FunctionDefinitionNode) node);
            }
            if (node instanceof FunctionCallNode) {
                return interpretFunctionCallNode((FunctionCallNode) node);
            }
            if (node instanceof BooleanNode) {
                return ((BooleanNode) node).getValue();
            } 
            if (node instanceof ForLoopNode) {
                return interpretForLoopNode((ForLoopNode) node);
            } 
            if (node instanceof PrintNode) {
                return interpretPrintNode((PrintNode) node);
            } 
            if (node instanceof VariableDeclarationNode) {
                return interpretVariableDeclarationNode((VariableDeclarationNode) node);
            } 
            if (node instanceof ReturnNode) {
                return interpretReturnNode((ReturnNode) node);
            } 
            if (node instanceof WhileNode) {
                return interpretWhileNode((WhileNode) node);
            }
            if (node instanceof FunctionDefinitionNode) {
                return interpretFunctionDefinitionNode((FunctionDefinitionNode) node);
            } 
            if (node instanceof NumberNode) {
                return ((NumberNode) node).getValue();
            } 
            if (node instanceof StringNode) {
                return ((StringNode) node).getValue();
            }
            if (node instanceof ForLoopNode) {
                return interpretForLoopNode((ForLoopNode) node);
            }
            else if (node instanceof ComparisonNode) {
                return interpretComparisonNode((ComparisonNode) node);
            }
    
            if (node instanceof BinaryOperationNode) {
                BinaryOperationNode binaryOperation = (BinaryOperationNode) node;
                Object leftValue = interpretNode(binaryOperation.getLeft());
                Object rightValue = interpretNode(binaryOperation.getRight());
                String operator = binaryOperation.getOperator();
            
                // 🔹 Handle multiplication with strings (e.g., "Hello" * 3 → "HelloHelloHello")
                if (operator.equals("*") && leftValue instanceof String && rightValue instanceof Integer) {
                    String value = (String) leftValue;
                    int repeat = (int) rightValue;
                    value = processEscapeSequences(value);
                    return value.repeat(repeat);
                }
            
                // 🔹 Handle numeric operations (ensure correct type)
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    boolean isFloat = (leftValue instanceof Double || rightValue instanceof Double);
                    double left = ((Number) leftValue).doubleValue();
                    double right = ((Number) rightValue).doubleValue();
            
                    double result = 0; // ✅ Initialize result
            
                    switch (operator) {
                        case "+": result = left + right; break;
                        case "-": result = left - right; break;
                        case "*": result = left * right; break;
                        case "/": 
                            if (right == 0) throw new RuntimeException("Division by zero error");
                            result = left / right;
                            isFloat = true; // Division always results in float
                            break;
            
                        case "<": return left < right;
                        case ">": return left > right;
                        case "==": return left == right;
                        case "!=": return left != right;
                        case "<=": return left <= right;
                        case ">=": return left >= right;
                        default: throw new RuntimeException("Unknown operator: " + operator);
                    }
            
                    // 🔹 Return Integer if both operands were originally integers
                    if (!isFloat && leftValue instanceof Integer && rightValue instanceof Integer) {
                        return (int) result;
                    }
                    return result; // Otherwise, return float
                }
            
                // 🔹 Handle logical operations (AND, OR)
                if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
                    boolean left = (boolean) leftValue;
                    boolean right = (boolean) rightValue;
            
                    switch (operator) {
                        case "&&": return left && right;
                        case "||": return left || right;
                        default: throw new RuntimeException("Unknown operator: " + operator);
                    }
                }
            
                throw new RuntimeException("Unsupported operation: " + operator);
            }
            
            if (node instanceof UnaryOperationNode) {
                UnaryOperationNode unaryOperation = (UnaryOperationNode) node;
                Object operandValue = interpretNode(unaryOperation.getOperand());
        
                switch (unaryOperation.getOperator()) {
                    case "!": return !(boolean) operandValue;
                    case "~": return ~(int) operandValue;
        
                    default: throw new RuntimeException("Unknown unary operator: " + unaryOperation.getOperator());
                }
            } 
            if (node instanceof VariableNode) {
                return interpretVariableNode((VariableNode) node);
            }
            throw new RuntimeException("Unknown node type: " + node.getClass());
        }
        
        private Boolean interpretComparisonNode(ComparisonNode node) {
            Object left = interpretNode(node.getLeft());
            Object right = interpretNode(node.getRight());
        
            if (left instanceof Integer && right instanceof Integer) {
                int leftValue = (Integer) left;
                int rightValue = (Integer) right;
        
                switch (node.getOperator()) {
                    case "<":
                        return leftValue < rightValue;
                    case ">":
                        return leftValue > rightValue;
                    case "==":
                        return leftValue == rightValue;
                    case "!=":
                        return leftValue != rightValue;
                    case "<=":
                        return leftValue <= rightValue;
                    case ">=":
                        return leftValue >= rightValue;
                    default:
                        throw new RuntimeException("Unsupported operator: " + node.getOperator());
                }
            }
        
            throw new RuntimeException("Operands must be integers");
        }
    
        private Object interpretBlockNode(BlockNode node) {
            Object result = "";
            for (Node statement : node.getStatements()) {
                result = interpretNode(statement);
            }
            return result;
        }
    
        private Object interpretForLoopNode(ForLoopNode node) {
            interpretNode(node.getInitialization()); 
        
            while (true) {
                Object conditionValue = interpretNode(node.getCondition());
        
                
                if (!(conditionValue instanceof Boolean)) {
                    throw new RuntimeException("For loop condition must be Boolean, found: " + conditionValue.getClass());
                }
        
                if (!(Boolean) conditionValue) {
                    break; 
                }
        
                interpretBlockNode(node.getBody()); 
        
                interpretNode(node.getUpdate()); 
            }
        
            return null;
        }
        
        private String processStringInterpolation(String text) {
            
            for (String varName : variables.keySet()) {
                String placeholder = "$" + varName; 
                if (text.contains(placeholder)) {
                    text = text.replace(placeholder, variables.get(varName).toString());
                }
            }
            return text;
        }
        
        private Object interpretPrintNode(PrintNode node) {
            Node valueNode = node.getValue();
            Object valuee = interpretNode(node.getValue());
            if (valuee instanceof String) {
                valuee = processStringInterpolation((String) valuee);
            
                String value = (String) interpretNode(valueNode);
                value = processEscapeSequences((String) valuee);
                System.out.print(value);
                System.out.print(processEscapeSequences("_reset"));
            }
            else if (valueNode instanceof StringNode) {
                String value = (String) interpretNode(valueNode);
                value = processEscapeSequences(value);
                System.out.print(value);
                System.out.print(processEscapeSequences("_reset"));
                return ""; 
            } else {
                Object value = interpretNode(valueNode);
                System.out.print(value);
                return ""; 
            }
                        return valuee;
        }
    
        private Object interpretVariableDeclarationNode(VariableDeclarationNode node) {
            String varName = node.getVarName();
            Node valueNode = node.getValue();
        
            if (valueNode instanceof NumberNode) {
                int value = ((NumberNode) valueNode).getValue();
                variables.put(varName, value);
            } else if (valueNode instanceof StringNode) {
                String value = ((StringNode) valueNode).getValue();
                value = processEscapeSequences(value); 
                variables.put(varName, value);
            } else if (valueNode instanceof BooleanNode) {
                boolean value = ((BooleanNode) valueNode).getValue();
                variables.put(varName, value);
            } else if (valueNode instanceof InputNode) {
                variables.put(varName,interpretInputNode((InputNode) valueNode)); 
            }else if (valueNode instanceof VariableNode) {
                String referencedVar = ((VariableNode) valueNode).getName();
                if (variables.containsKey(referencedVar)) {
                    Object value = variables.get(referencedVar);
                    variables.put(varName, value);
                } else {
                    throw new RuntimeException("Undefined variable: " + referencedVar);
                }
            } else {
                
                Object value = interpretNode(valueNode);
                variables.put(varName, value);
                
                return "";
            }
        
            return ""; 
        }
        
    
        private int interpretReturnNode(ReturnNode node) {
            return (int) interpretNode(node.getValue());
        }
    
        private Object interpretFunctionDefinitionNode(FunctionDefinitionNode node) {
            functions.put(node.getFunctionName(), node); 
            return null;
        }
        
        private Object interpretFunctionCallNode(FunctionCallNode node) {
            if (!functions.containsKey(node.getFunctionName())) {
                throw new RuntimeException("Undefined function: " + node.getFunctionName());
            }
        
            FunctionDefinitionNode function = functions.get(node.getFunctionName());
        
            
            if (function.getParameters().size() != node.getArguments().size()) {
                throw new RuntimeException("Function '" + function.getFunctionName() + "' expects " + function.getParameters().size() + " arguments, but got " + node.getArguments().size());
            }
        
            
            Map<String, Object> previousVars = new HashMap<>(variables);
        
            
            for (int i = 0; i < function.getParameters().size(); i++) {
                String paramName = function.getParameters().get(i);
                Object argumentValue = interpretNode(node.getArguments().get(i));
                variables.put(paramName, argumentValue);
            }
        
            
            Object returnValue = interpretBlockNode(function.getBody());
        
            
            variables = previousVars;
    
        return returnValue;
    }
    
    private Object interpretVariableNode(VariableNode node) {
        String varName = node.getName();
        if (variables.containsKey(varName)) {
            Object value = variables.get(varName);
            return value;
        } else if (stringVariables.containsKey(varName)) {
            String value = stringVariables.get(varName);
            return value;
        } else {
            throw new RuntimeException("Undefined variable: " + varName);
        }
    }

    private String processEscapeSequences(String value) {
        
        value = value.replace("_blue", "\033[34m")
                     .replace("_black", "\033[30m")
                     .replace("_green", "\033[32m")
                     .replace("_red", "\033[31m")
                     .replace("_yellow", "\033[33m")
                     .replace("_orange", "\033[38;5;214m")  
                     .replace("_violet", "\033[35m")
                     .replace("_cyan", "\033[36m")          
                     .replace("_white", "\033[37m");        
    
        
        value = value.replace("_bright_red", "\033[1;31m")
                     .replace("_bright_green", "\033[1;32m")
                     .replace("_bright_blue", "\033[1;34m")
                     .replace("_bright_yellow", "\033[1;33m")
                     .replace("_bright_white", "\033[1;37m");
    
        
        value = value.replace("_bold", "\033[1m")
                     .replace("_italic", "\033[3m")
                     .replace("_underline", "\033[4m")
                     .replace("_strike", "\033[9m")
                     .replace("_reverse", "\033[7m")  
                     .replace("_blink", "\033[5m")    
                     .replace("_hidden", "\033[8m");  
    
        
        value = value.replace("_INFO", "\033[32m[INFO] \033[0m")  
                     .replace("_WARN", "\033[33m[WARN] \033[0m")  
                     .replace("_ERROR", "\033[31m[ERROR] \033[0m") 
                     .replace("_DEBUG", "\033[36m[DEBUG] \033[0m"); 
    
        
        value = value.replace("_n", "\n")    
                     .replace("_t", "\t")    
                     .replace("_w", " ");    
    
        
        value = value.replace("_align_left", "\033[0G")  
                     .replace("_align_right", "\033[60G") 
                     .replace("_center", "\033[C");    
    
        
        value = value.replace("_at", "\u0040") 
                     .replace("_hash", "\u0023")  
                     .replace("_star", "\u002A");  
    
        
        value = value.replace("_LOG_DEBUG", "\033[36m[DEBUG] \033[0m")
                     .replace("_LOG_INFO", "\033[32m[INFO] \033[0m")
                     .replace("_LOG_WARNING", "\033[33m[WARNING] \033[0m")
                     .replace("_LOG_ERROR", "\033[31m[ERROR] \033[0m")
                     .replace("_LOG_FATAL", "\033[1;31m[FATAL] \033[0m");
    
        
        value = value.replace("_reset", "\033[0m")
                     .replace("_reset_color", "\033[39m")  
                     .replace("_reset_bg", "\033[49m");    
    
        
        return value;
    }
    
    

}
