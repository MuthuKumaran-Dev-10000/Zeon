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
    private Node parseClassDeclaration() {
        consume("class");
        String className = consume(TokenType.IDENTIFIER).getValue();
        consume("{");
    
        List<VariableNode> properties = new ArrayList<>();
        List<FunctionDefinitionNode> methods = new ArrayList<>();
        FunctionDefinitionNode constructor = null;
    
        while (!match("}")) {
            if (match("fn")) {
                methods.add((FunctionDefinitionNode) parseFunctionDefinition());  // Add function as private method
            } else if (match(TokenType.IDENTIFIER)) {
                properties.add((VariableNode) parseVariableDeclaration());  // Class variables
            } else if (match(className)) {  // Constructor (same as class name)
                constructor = parseConstructor(className);
            } else {
                throw new RuntimeException("Unexpected token in class: " + peek());
            }
        }
        consume("}");
    
        return new ClassNode(className, properties, constructor, methods);
    }
    private Node parseTryCatchStatement() {
        // consume("try");
        consume("{");
        BlockNode tryBlock = new BlockNode(parseBlock());
        // consume("}");
    
        String exceptionVariable = null;
        BlockNode catchBlock = null;
        if (match("catch")) {
            // consume("catch");
            consume("(");
            exceptionVariable = consume(TokenType.IDENTIFIER).getValue();
            consume(")");
            consume("{");
            catchBlock = new BlockNode(parseBlock());
            // consume("}");
        }
    
        BlockNode finallyBlock = null;
        if (match("finally")) {
            // consume("finally");
            consume("{");
            finallyBlock = new BlockNode(parseBlock());
            // consume("}");
        }
    
        return new TryCatchNode(tryBlock, exceptionVariable, catchBlock, finallyBlock);
    }
    
    private FunctionDefinitionNode parseConstructor(String className) {
        consume(className);  // Consume constructor name
        consume("(");
        List<String> params = parseParameterList();
        consume(")");
    
        consume("{");
        List<Node> body = parseBlock();
        consume("}");
    
        return new FunctionDefinitionNode(className, params, new BlockNode(body));
    }
    private Node parseFileHandlingStatement() {
        // consume("with");  // Ensure 'with' is present
        consume("(");  // Opening bracket for parameters
        
        Node fileName = parseExpression();  // First parameter (filename)
        consume(",");  // Ensure a comma is present
    
        // if (!match(TokenType.FILE_MODE)) {
        //     throw new RuntimeException("Expected file mode (r, w, x) in file handling.");
        // }
    
        String mode = consume(TokenType.FILE_MODE).getValue();
        consume(")");  // Closing bracket
        consume("as");  // Ensure 'as' is present
    
        String variableName = consume(TokenType.IDENTIFIER).getValue();  // File variable
        consume("{");  // Opening brace for block
    
        List<Node> body = new ArrayList<>();
        while (!match("}")) {
            body.add(parseStatement());  // Parse file operations inside the block
        }
        // consume("}");  // Closing brace
    
        return new WithNode(fileName, mode, variableName, new BlockNode(body));
    }
    private Node parseSwitchStatement() {
        // consume(TokenType.SWITCH);  // ✅ Consume `switch`
        consume("(");
        Node switchExpression = parseExpression();  // ✅ Parse switch variable/expression
        consume(")");
        consume("{");  // ✅ Opening `{` for switch block
    
        System.out.println("[DEBUG] Entered switch statement with value: " + switchExpression);
    
        List<CaseNode> cases = new ArrayList<>();
        Node defaultCase = null;
        int caseCount = 0;  // ✅ Counter for how many cases are processed
        
        while (position < tokens.size()) {  // ✅ Ensure within bounds
            
            if (position >= tokens.size()) {
                throw new RuntimeException("Unexpected end of input inside switch-case.");
            }
    
            if (match(TokenType.CASE)) {  // ✅ Handle `case`
                // consume(TokenType.CASE); // ✅ Explicitly consume `case`
    
                if (position >= tokens.size()) {
                    throw new RuntimeException("Unexpected end of input after `case`.");
                }
    
                Node caseValue = parseExpression();
                consume("{");  // ✅ Ensure `{` starts case body
    
                // System.out.println("[DEBUG] Entered case with value: " + caseValue);
    
                List<Node> caseBody = new ArrayList<>();
                while (position < tokens.size() && !match("}")) {  
                    caseBody.add(parseStatement());
                    // if (match("}")) {
                    //     System.out.println("Found } at case end");
                    // }
                }
    
                
                // consume("}");  // ✅ Ensure case block closes
    
                // System.out.println("[DEBUG] Exited case with value: " + caseValue);
                caseCount++; // ✅ Increase processed case count
                cases.add(new CaseNode(caseValue, new BlockNode(caseBody)));
            } 
            else if (match(TokenType.DEFAULT)) {  // ✅ Handle `default`
                // consume(TokenType.DEFAULT);  // ✅ Explicitly consume `default`
                consume("{");
    
                // System.out.println("[DEBUG] Entered default case");
    
                List<Node> defaultBody = new ArrayList<>();
                while (position < tokens.size() && !match("}")) {  
                    defaultBody.add(parseStatement());
                    if (match("}")) {
                        // System.out.println("} found at default");
                    }
                }
    
                
                // consume("}");  // ✅ Ensure default block closes
    
                // System.out.println("[DEBUG] Exited default case");
                defaultCase = new BlockNode(defaultBody);
            } 
            
            // else if (check(TokenType.DICT_CLOSE)) {  // ✅ Ensure switch closes properly
            //     consume("}");  // ✅ NOW CONSUME `}`
            //     System.out.println("[DEBUG] Exited switch statement. Processed " + caseCount + " cases.");
            //     return new SwitchNode(switchExpression, cases, defaultCase);
            // }
            else {  
                // throw new RuntimeException("Unexpected token in switch-case: " + tokens.get(position));
                return new SwitchNode(switchExpression, cases, defaultCase);
            }
            
        }
        return new SwitchNode(switchExpression, cases, defaultCase);
        
        // throw new RuntimeException("Expected closing `}` for switch-case block.");  // 🔴 Final fallback error
    }
    
    
    
    
    
    
    private Node parseWithStatement() {
        consume("(");
        Node fileName = parseExpression(); // Parse "filename.txt"
        consume(",");
        Token modeToken = consume(TokenType.FILE_MODE); // Parse "r", "w", "x"
        consume(")");
        consume(TokenType.AS);
        String variableName = consume(TokenType.IDENTIFIER).getValue(); // Variable f
        consume("{");
    
        List<Node> body = new ArrayList<>();
        while (!match("}")) {
            body.add(parseStatement());
        }
        consume("}");
    
        return new WithNode(fileName, modeToken.getValue(), variableName, new BlockNode(body));
    }
    private Node parseWriteFunction() {
        // consume("write");  // Ensure 'write' is detected
        consume("(");
        Node fileVariable = parseExpression();
        consume(",");
        Node content = parseExpression();
        consume(")");
        consume(";");
    
        return new WriteNode(fileVariable, content);
    }
    private Node parseWebBlock() {
        consume("`");  // Start of the Web block
        StringBuilder htmlContent = new StringBuilder();
    
        // Collect all lines until the closing backtick
        while (!tokens.get(position).getValue().equals("`")) {
            htmlContent.append(tokens.get(position).getValue()).append("\n");
            position++;  // Move to the next token
        }
    
        consume("`");  // End of the Web block
        return new WebNode(htmlContent.toString().trim());
    }
    
    private Node parseStatement() {
        if (match("type")) {
            return parseTypeStatement();
        }
        if (match("with")) {
            return parseFileHandlingStatement();
        }else if (match("write")) {
            return parseWriteFunction();
        }
        if (match(TokenType.SWITCH)) {  // ✅ Add switch support here!
            return parseSwitchStatement();
        }
        if (match("try")) {
            return parseTryCatchStatement(); 
        }
        if (match(TokenType.JAVA_BLOCK)) {
            return new CodeBlockNode(TokenType.JAVA_BLOCK, tokens.get(position - 1).getValue(),null);
        }
        if (match(TokenType.PYTHON_BLOCK)) {
            return new CodeBlockNode(TokenType.PYTHON_BLOCK, tokens.get(position - 1).getValue(),null);
        }
        if (match(TokenType.WEB_BLOCK)) {
    return new CodeBlockNode(TokenType.WEB_BLOCK, tokens.get(position - 1).getValue(), null);
}

        if (match(TokenType.WITH)) {
            return parseWithStatement();
        }
        if (match(TokenType.RETURN)) {
        return parseReturnStatement();
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
        } 
        else if(match(TokenType.IDENTIFIER)){
            return parseVariableDeclaration();

        }
        else {
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
        
        if (check(TokenType.IDENTIFIER) && tokens.get(position + 1).getValue().equals("(")) {
            String varNamee = consume(TokenType.IDENTIFIER).getValue();
            Node functionCall = parseFunctionCall(varNamee);
             // Debug message
            consume(";"); // Ensure semicolon after function call
            return new VariableDeclarationNode(varName, functionCall);
        }
        Node value = parseExpression();
        consume(";");
        return new VariableDeclarationNode(varName, value);
    }

    private Node parseFunctionCall(String functionName) {
        // consume("(");  // Consume `(`
    
        // String functionName = consume(TokenType.IDENTIFIER).getValue();
    
        
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
        // consume(";");
        return new FunctionCallNode(functionName, arguments);
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
        consume("("); // ✅ Consume '('
    
        // ✅ Manually parse initialization: `let i : 0`
        // if (!match(TokenType.IDENTIFIER)) {
        //     throw new RuntimeException("Expected 'let' keyword in for loop initialization.");
        // }
        if(match("let")){
            // consume("let");
        }
        // consume(TokenType.IDENTIFIER); // `let`
    
        String varName = consume(TokenType.IDENTIFIER).getValue(); // `i`
        consume(":");
        Node initialValue = parseExpression(); // `0`
    
        VariableDeclarationNode initialization = new VariableDeclarationNode(varName, initialValue);
    
        if (!match(",")) {
            throw new RuntimeException("Expected ',' after for loop initialization.");
        }
        // consume(",");
    
        // ✅ Parse condition: `i <= 10`
        Node condition = parseExpression();
    
        if (!match(",")) {
            throw new RuntimeException("Expected ',' after for loop condition.");
        }
        // consume(",");
    
        // ✅ Manually parse increment: `let i : i + 1`
        // if (!match(TokenType.IDENTIFIER)) {
        //     throw new RuntimeException("Expected 'let' keyword in for loop increment.");
        // }
        // consume(TokenType.IDENTIFIER); // `let`
        if(match("let")){
            // consume("let");
        }
        String incVarName = consume(TokenType.IDENTIFIER).getValue(); // `i`
        consume(":");
        Node incrementValue = parseExpression(); // `i + 1`
    
        VariableDeclarationNode increment = new VariableDeclarationNode(incVarName, incrementValue);
    
        if (!match(")")) {
            throw new RuntimeException("Expected ')' at end of for loop declaration.");
        }
        // consume(")");
        
        if (!match("{")) {
            throw new RuntimeException("Expected '{' to start for loop body.");
        }
        // consume("{");
        // System.out.println(tokens.get(position).getValue());
        // ✅ Parse loop body as `BlockNode`
        List<Node> bodyStatements = parseBlock();
        BlockNode body = new BlockNode(bodyStatements);
        // System.out.println(body);
        
        // if (!match("}")) {
        //     throw new RuntimeException("Expected '}' to close for loop body.");
        // }
        // consume("}");
    
        return new ForLoopNode(initialization, condition, increment, body);
    }
    
    
    
    
    

    private Node parseReturnStatement() {
    // consume(TokenType.RETURN); // Consume the `return` keyword
    Node returnValue = parseExpression(); // Parse the expression to return
    consume(";"); // Consume the semicolon
    return new ReturnNode(returnValue);
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
        while (match("+") || match("-") ) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }
    //     while (match("(")) { // If it's a function call
    //     left = parseFunctionCall(((VariableNode) left).getName());
    // }
        return left;
    }

    private Node parseFactor() {
        Node left = parseUnary();
        while (match("*") || match("/")||match("%")) {
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
    private List<Node> parseBlock() {
        List<Node> statements = new ArrayList<>();
        while (!match("}")) {
            statements.add(parseStatement());
        }
        // consume("}");
        return statements;
    }
    
    private List<String> parseParameterList() {
        List<String> params = new ArrayList<>();
        while (!match(")")) {
            params.add(consume(TokenType.IDENTIFIER).getValue());
            if (!match(")")) {
                consume(","); // Consume comma
            }
        }
        return params;
    }
    
}
