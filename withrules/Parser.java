package withrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public Node parse() {
        List<Node> statements = new ArrayList<>();
        while (position < tokens.size()) {
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private Node parseCondition() {
    
    List<Token> conditionTokens = new ArrayList<>();
    while (position < tokens.size() && !tokens.get(position).getValue().equals(")")) {
        conditionTokens.add(tokens.get(position));
        position++;
    }

    
    Parser conditionParser = new Parser(conditionTokens);
    return conditionParser.parseExpression(); 
}
    
    private Node parseIfElseStatement() {
        
    
        
        if (tokens.get(position).getValue().equals("(")) {
            position++; 
        } else {
            throw new RuntimeException("Expected '(' after 'if'");
        }
    
        
        Node condition = parseCondition();
        if (tokens.get(position).getValue().equals(")")) {
            System.out.println("Token :"+tokens.get(position).getValue());
            position++; 
        } else {
            System.out.println("Token :"+tokens.get(position).getValue());
            throw new RuntimeException("Expected ')' after condition in 'if'");
        }
    
        consume("{"); 
        List<Node> ifBody = new ArrayList<>();
        while (!match("}")) {
            ifBody.add(parseStatement());
        }
        
    
        
    ElseIfNode elseIfNode = null;
    ElseIfNode lastElseIfNode = null;

    
    while (match("elseif")) {
        
        

        if (tokens.get(position).getValue().equals("(")) {
            position++; 
        } else {
            throw new RuntimeException("Expected '(' after 'else if'");
        }


        
        Node elseIfCondition = parseCondition();

        if (tokens.get(position).getValue().equals(")")) {
            position++; 
        } else {
            throw new RuntimeException("Expected ')' after condition in 'else if'");
        }
        System.out.println("enetred elseif condition");
        
        consume("{");
        List<Node> elseIfBody = new ArrayList<>();
        while (!match("}")) {
            elseIfBody.add(parseStatement());
        }
        System.out.println("enetred elseif body");
        
        ElseIfNode currentElseIf = new ElseIfNode(elseIfCondition, new BlockNode(elseIfBody), null);
        if (lastElseIfNode != null) {
            lastElseIfNode.setNextElseIfNode(currentElseIf);
        }
        else {
            elseIfNode = currentElseIf;  
        }

        
        lastElseIfNode = currentElseIf;
    }

    
    BlockNode elseBlock = null;
    if (match("else")) {
        
        consume("{"); 
        List<Node> elseBody = new ArrayList<>();
        while (!match("}")) {
            elseBody.add(parseStatement());
        }
        elseBlock = new BlockNode(elseBody);
    }

    
    return new IfElseNode(condition, new BlockNode(ifBody), elseIfNode, elseBlock);
    }
    
    private Node parseTuple() {
        List<Node> elements = new ArrayList<>();
        
    
        while (!match(")")) { 
            elements.add(parseExpression());
            if (match(",")) {
                consume(","); 
            }
        }
    
        consume(")"); 
        return new TupleNode(elements);
    }
    private Node parseDictionary() {
    Map<Node, Node> pairs = new HashMap<>();
    

    while (!match("}")) { 
        Node key = parseExpression(); 
        consume(":"); 
        Node value = parseExpression(); 

        pairs.put(key, value);

        if (match(",")) {
            
        }
    }

    
    return new DictNode(pairs);
}

    
    private Node parseList() {
        List<Node> elements = new ArrayList<>();
        
    
        while (!match("]")) { 
            elements.add(parseExpression());
            if (match(",")) {
                
                
            }
        }
    
        
        return new ListNode(elements);
    }

    
    private Node parseCodeBlock(TokenType type) {
        consume(type.name().toLowerCase()); 
        consume("`"); 
        
        StringBuilder codeBlock = new StringBuilder();
        String sendVariable = null;
    
        while (!match("`")) {
            if (match(TokenType.SEND)) {
                consume("send"); 
                consume(":"); 
                sendVariable = tokens.get(position).getValue(); 
                position++;
            } else {
                codeBlock.append(tokens.get(position).getValue()).append(" ");
                position++; 
            }
        }
    
        consume("`"); 
    
        return new CodeBlockNode(type, codeBlock.toString().trim(), sendVariable);
    }
    
    
    private Node parseStatement() {
        if (match("type")) {
            return parseTypeStatement();
        }
        if (match(TokenType.JAVA_BLOCK)) {
            return new CodeBlockNode(TokenType.JAVA_BLOCK, tokens.get(position - 1).getValue(),null);
        }
        if (match(TokenType.PYTHON_BLOCK)) {
            return new CodeBlockNode(TokenType.PYTHON_BLOCK, tokens.get(position - 1).getValue(),null);
        }
        
        
        if (match("let")) {
            return parseVariableDeclaration();
        }else if (match("if")) {
            return parseIfElseStatement();
        }else if (match("print")) {
            return parsePrintStatement();
        }else if (match("while")) {
            return parseWhileStatement();
        }else if (match("fn")) {  
            return parseFunctionDefinition();
        } else if (lookAheadIsFunctionCall()) {  
            return parseFunctionCall();
        }  else if (match("for")) {
            return parseForLoop(); 
        } else if (match("sendruva")) {
            return parseReturnStatement();
        } else if (match("vetriprint")) {
            return parseFunctionDefinition();
        } else if (tokens.get(position).getType() == TokenType.IDENTIFIER) {
            return parseAssignmentOrExpression();
        } else {
            throw new RuntimeException("Unexpected token: " + tokens.get(position));
        }
    }

    private boolean lookAheadIsFunctionCall() {
        return position < tokens.size() - 1 &&
               tokens.get(position).getType() == TokenType.IDENTIFIER &&
               tokens.get(position + 1).getValue().equals("(");
    }
    private Node parseVariableDeclaration() {
        String varName = consume(TokenType.IDENTIFIER).getValue();
        consume(":");
        Node value = parseExpression();
        consume(";");
        return new VariableDeclarationNode(varName, value);
    }
    private Node parseWhileStatement() {
    

    if (!match("(")) {
        throw new RuntimeException("Expected '(' after 'while'");
    }

    Node condition = parseExpression(); 

    if (!match(")")) {
        throw new RuntimeException("Expected ')' after condition in 'while'");
    }

    if (!match("{")) {
        throw new RuntimeException("Expected '{' to start 'while' block");
    }

    List<Node> whileBody = new ArrayList<>();
    while (!tokens.get(position).getValue().equals("}")) {
        whileBody.add(parseStatement());
    }

    if (!match("}")) {
        throw new RuntimeException("Expected '}' to close 'while' block");
    }

    return new WhileNode(condition, new BlockNode(whileBody));
}

    private Node parsePrintStatement() {
        consume("(");
        Node value = parseExpression();
        consume(")");
        consume(";");
        return new PrintNode(value);
    }
    private Token previous() {
        return tokens.get(position - 1); 
    }
    
    private Node parseForLoop() {
        
        consume("("); 
    
        
        Node initialization = parseStatement(); 
        
        
        if (!match(TokenType.IDENTIFIER) && !match(TokenType.NUMBER)) {  
            throw new RuntimeException("Expected condition expression after initialization in for loop");
        }
        System.out.println("Token past"+tokens.get(position-1));
        System.out.println("Token curent"+tokens.get(position));
        System.out.println("Token next"+tokens.get(position+1));
        Node condition = parseExpression();
        
        consume(";"); 
    
        
        Node update = parseStatement(); 
        
        consume(")"); 
    
        consume("{"); 
    
        List<Node> body = new ArrayList<>();
        while (!match("}")) {
            body.add(parseStatement());
        }
        consume("}"); 
    
        return new ForLoopNode(initialization, condition, update, new BlockNode(body));
    }
    
    
    
    

    private Node parseReturnStatement() {
        Node value = parseExpression();
        consume(";");
        return new ReturnNode(value);
    }
    private Node parseFunctionDefinition() {
        
        String functionName = consume(TokenType.IDENTIFIER).getValue(); 
        consume("("); 
    
        
        List<String> parameters = new ArrayList<>();
        if (!match(")")) {
            do {
                parameters.add(consume(TokenType.IDENTIFIER).getValue());
            } while (match(","));
            consume(")");
        } else {
            consume(")");
        }
    
        consume("{"); 
        
        List<Node> functionBody = new ArrayList<>();
        while (!match("}")) {
            functionBody.add(parseStatement());
        }
        
    
        return new FunctionDefinitionNode(functionName, parameters, new BlockNode(functionBody));
    }
    private Node parseFunctionCall() {
        
        String functionName = consume(TokenType.IDENTIFIER).getValue();
    
        
        if (!match("(")) {
            throw new RuntimeException("Expected '(' after function name: " + functionName);
        }
    
        
        List<Node> arguments = new ArrayList<>();
        if (!match(")")) { 
            do {
                arguments.add(parseExpression());
            } while (match(",")); 
            consume(")");
        }
        consume(";");
        return new FunctionCallNode(functionName, arguments);
    }

    private Node parseTypeStatement() {
        
        Node value = parseExpression(); 
        consume(";"); 
        return new TypeNode(value);
    }
    
    private Node parseTypeCast(TokenType type) {
        
        consume("("); 
        Node value = parseExpression(); 
        consume(")"); 
        return new TypeCastNode(type, value);
    }
    
    private VariableDeclarationNode parseAssignmentOrExpression() {
        Node left = parseExpression();
        if (tokens.get(position).getValue().equals(":")) {
            
            position++; 
            Node value = parseExpression();
            consume(";");
            return new VariableDeclarationNode(((VariableNode) left).getName(), value);
        } else {
            
            consume(";");
            
        }
        return null;
    }

    private Node parseExpression() {
        Node left = parseTerm();
        while (match(TokenType.EQUAL_EQUAL) || match(TokenType.NOT_EQUAL) ||
               match(TokenType.LESS_THAN) || match(TokenType.GREATER_THAN) ||
               match(TokenType.LESS_THAN_EQUAL) || match(TokenType.GREATER_THAN_EQUAL)) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseTerm();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }
    

    private Node parseTerm() {
        Node left = parseFactor();
        while (match("+") || match("-")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private Node parseFactor() {
        Node left = parseUnary();
        while (match("*") || match("/")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseUnary();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }
    private Node parseUnary() {
        if (match(TokenType.LOGICAL_NOT, TokenType.BITWISE_NOT)) {
            String operator = previous().getValue(); 
            Node operand = parseUnary(); 
            return new UnaryOperationNode(operator, operand); 
        }
        return parsePrimary(); 
    }
    
    private Node parsePrimary() {
        if (match(TokenType.TYPE)) {
            return parseTypeStatement();
        }
        if (match(TokenType.CAST_LIST)) {
            return parseTypeCast(TokenType.CAST_LIST);
        }
        if (match(TokenType.CAST_STRING)) {
            return parseTypeCast(TokenType.CAST_STRING);
        }
        if (match(TokenType.CAST_NUMBER)) {
            return parseTypeCast(TokenType.CAST_NUMBER);
        }
        if (match(TokenType.CAST_FLOAT)) {
            return parseTypeCast(TokenType.CAST_FLOAT);
        }
        
        if (match(TokenType.LIST_OPEN)) {  
            return parseList();
        }
        if (match(TokenType.TUPLE_OPEN)) {  
            return parseTuple();
        }
        if (match(TokenType.DICT_OPEN)) {  
            return parseDictionary();
        }
        if (match(TokenType.IDENTIFIER)) {
            Node left = new VariableNode(tokens.get(position - 1).getValue());

            
            if (match(TokenType.LESS_THAN) || match(TokenType.GREATER_THAN) ||
                match(TokenType.LESS_THAN_EQUAL) || match(TokenType.GREATER_THAN_EQUAL) ||
                match(TokenType.EQUAL_EQUAL) || match(TokenType.NOT_EQUAL)) {
                
                Token operator = tokens.get(position - 1); 
                Node right = parsePrimary(); 
                
                return new BinaryOperationNode(left, operator.getValue(), right);
            }
    
            return left;
        
        }
        if (match(TokenType.INPUT)) {  
        consume("(");
        Node prompt = parseExpression();
        consume(")");
        return new InputNode(prompt);
        }
        if (match(TokenType.TRUE)) {
            return new BooleanNode(true);
        } else if (match(TokenType.FALSE)) {
            return new BooleanNode(false);
        }
        else if (match(TokenType.FLOAT)) {  // ✅ **Add Support for Float**
            return new FloatNode(Double.parseDouble(tokens.get(position - 1).getValue()));
        }
        else if (match(TokenType.NUMBER)) {
            return new NumberNode(Integer.parseInt(tokens.get(position - 1).getValue()));
        } else if (match(TokenType.STRING)) {
            return new StringNode(tokens.get(position - 1).getValue());
        }  else if (match("(")) {
            Node expression = parseExpression();
            consume(")");
            return expression;
        } else {
            
            throw new RuntimeException("Unexpected token: " + tokens.get(position));
        }
    }

    private boolean match(String expected) {
        if (position < tokens.size() && tokens.get(position).getValue().equals(expected)) {
            position++;
            return true;
        }
        return false;
    }

    private boolean match(TokenType type) {
        if (position < tokens.size() && tokens.get(position).getType() == type) {
            position++;
            return true;
        }
        return false;
    }
    private boolean isAtEnd() {
        return position >= tokens.size();
    }
    
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }
    private Token peek() {
        return tokens.get(position);
    }
    private Token advance() {
        if (!isAtEnd()) {
            position++;
        }
        return previous();
    }
    
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }
    
    private Token consume(TokenType type) {
        if (position < tokens.size() && tokens.get(position).getType() == type) {
            return tokens.get(position++);
        }
        throw new RuntimeException("Expected token type: " + type + " but found: " + tokens.get(position));
    }

    private void consume(String value) {
        if (position < tokens.size() && tokens.get(position).getValue().equals(value)) {
            position++;
        } else {
            throw new RuntimeException("Expected token: " + value + " but found: " + tokens.get(position));
        }
    }

    private Token consume(TokenType type, String errorMessage) {
        if (position < tokens.size() && tokens.get(position).getType() == type) {
            return tokens.get(position++);
        }
        throw new RuntimeException("Expected token type: " + type + " but found: " + tokens.get(position));
    }
    
}
