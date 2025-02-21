package withrules;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            
            if (Character.isWhitespace(currentChar)) {
                position++;
            } else if (Character.isDigit(currentChar)) {
                tokens.add(tokenizeNumber());
            } else if (Character.isLetter(currentChar)) {
                tokens.add(tokenizeIdentifier());
            } else if (currentChar == '\"') {
                tokens.add(tokenizeString());
            }
            else if (currentChar == '?') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '?') {
                    
                    while (position < input.length() && input.charAt(position) != '\n') {
                        position++;
                    }
                } else {
                    tokens.add(tokenizeOperator());
                }
            } else {
                tokens.add(tokenizeOperator());
            }
        }
        return tokens;
    }
    
    

    // private Token tokenizeNumber() {
    //     StringBuilder buffer = new StringBuilder();
    //     while (position < input.length() && Character.isDigit(input.charAt(position))) {
    //         buffer.append(input.charAt(position));
    //         position++;
    //     }
    //     return new Token(TokenType.NUMBER, buffer.toString());
    // }
    private Token tokenizeNumber() {
        StringBuilder buffer = new StringBuilder();
        boolean isFloat = false; 
    
        while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            if (input.charAt(position) == '.') {
                if (isFloat) {
                    throw new RuntimeException("Unexpected second '.' in number");
                }
                isFloat = true; 
            }
            buffer.append(input.charAt(position));
            position++;
        }
    
        // If it's a float, store it as FLOAT type; otherwise, store as NUMBER
        if (isFloat) {
            return new Token(TokenType.FLOAT, buffer.toString());
        } else {
            return new Token(TokenType.NUMBER, buffer.toString());
        }
    }
    
    private Token tokenizeIdentifier() {
        StringBuilder buffer = new StringBuilder();
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            buffer.append(input.charAt(position));
            position++;
        }
        String value = buffer.toString();
        switch (value) {
            case "if":
                return new Token(TokenType.IF, value);
            case "type":
                return new Token(TokenType.TYPE, value);
            case "with": return new Token(TokenType.WITH, value);
            case "as": return new Token(TokenType.AS, value);
            case "write": return new Token(TokenType.WRITE, value);
            case "switch":
                return new Token(TokenType.SWITCH, value);
            case "case":
                return new Token(TokenType.CASE, value);
            case "default":
                return new Token(TokenType.DEFAULT, value);
            case "Web":
                return new Token(TokenType.WEB_BLOCK, value);
            
            case "r": case "w": return new Token(TokenType.FILE_MODE, value);
            case "else":
                return new Token(TokenType.ELSE, value);
            case "elseif":
                return new Token(TokenType.ELSE_IF, value);
            case "for":
                return new Token(TokenType.FOR, value);
            
            case "[":
                return new Token(TokenType.LIST_OPEN, "[");
                
            case "]":
                return new Token(TokenType.LIST_CLOSE, "]");
                
            case "(":
                return new Token(TokenType.TUPLE_OPEN, "(");
            case ")":
                return new Token(TokenType.TUPLE_CLOSE, ")");
                
            case "{":
                return new Token(TokenType.DICT_OPEN, "{");
                
            case "}":
                return new Token(TokenType.DICT_CLOSE, "}");
                
            case ":":
                return new Token(TokenType.COLON, ":");
                
            case ",":
                return new Token(TokenType.COMMA, ",");
            case "list":
                return new Token(TokenType.CAST_LIST, value);
            case "string":
                return new Token(TokenType.CAST_STRING, value);
            case "number":
                return new Token(TokenType.CAST_NUMBER, value);
            case "float":
                return new Token(TokenType.CAST_FLOAT, value);
            case "java":
                return tokenizeCodeBlock(TokenType.JAVA_BLOCK);
            case "class":
                return new Token(TokenType.CLASS, value);
            case "this": 
                return new Token(TokenType.THIS, value);
            case "new":
                return new Token(TokenType.NEW, value);
            case "python":
                return tokenizeCodeBlock(TokenType.PYTHON_BLOCK);
            
            case "try": 
                return new Token(TokenType.TRY, value);
            case "catch": 
                return new Token(TokenType.CATCH, value);
            case "finally": 
                return new Token(TokenType.FINALLY, value);
            
            
            case "true":
                return new Token(TokenType.TRUE, value);
            case "fn":
                return new Token(TokenType.FUNCTION, value);
            case "return":
                return new Token(TokenType.RETURN, value);
            
            case "false":
                return new Token(TokenType.FALSE, value);
            case "input":
                return new Token(TokenType.INPUT, value);
            case "while":
                return new Token(TokenType.WHILE, value);
            default:
                return new Token(TokenType.IDENTIFIER, value);
        }
    }
    private Token tokenizeCodeBlock(TokenType type) {
        StringBuilder blockCode = new StringBuilder();
        
        
if (type.name().equals("java_block")) {
    position += "java".length();
} else if (type.name().equals("python_block")) {
    position += "python".length();
}
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            char currentChar = input.charAt(position);
        if (currentChar == '.' || currentChar == '\n' || currentChar == '\t') {
            blockCode.append(currentChar);
        } else if (!Character.isLetterOrDigit(currentChar) && !Character.isWhitespace(currentChar) && "{};()+-*/<>=!&|".indexOf(currentChar) == -1) {
            throw new RuntimeException("Unexpected character: " + currentChar);
        } else {
            blockCode.append(currentChar);
        }
        position++;
        }
        if (position >= input.length() || input.charAt(position) != '`') {
            throw new RuntimeException("Expected '`' after " + type.name().toLowerCase());
        }
        position++; 
        while (position < input.length() && input.charAt(position) != '`') {
            blockCode.append(input.charAt(position));
            position++;
        }
        if (position >= input.length()) {
            throw new RuntimeException("Expected '>' to close " + type.name().toLowerCase() + " block");
        }
    
        position++; 
    
        return new Token(type, blockCode.toString().trim());
    }
    

    private Token tokenizeString() {
        StringBuilder buffer = new StringBuilder();
        position++; 
        while (position < input.length() && input.charAt(position) != '\"') {
            buffer.append(input.charAt(position));
            position++;
        }
        position++; 
        return new Token(TokenType.STRING, buffer.toString());
    }

    private Token tokenizeOperator() {
        char currentChar = input.charAt(position);
        
        if (currentChar == '.') {
            position++;
            return new Token(TokenType.DOT, ".");
        }
        
        
        
        if (currentChar == '[') {
            position++; 
            return new Token(TokenType.LIST_OPEN, "[");
        } else if (currentChar == ']') {
            position++; 
            return new Token(TokenType.LIST_CLOSE, "]");
        }
        if (currentChar == '{') {
            position++; 
            return new Token(TokenType.DICT_OPEN, "{");
        } else if (currentChar == '}') {
            position++; 
            return new Token(TokenType.DICT_CLOSE, "}");
        }
        if (currentChar == '<') {
            if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                position +=2;
                return new Token(TokenType.LESS_THAN_EQUAL, "<=");
            } else {
                position++;
                return new Token(TokenType.LESS_THAN, "<");
            }
        } else if (currentChar == '>') {
            if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                position+=2;
                return new Token(TokenType.GREATER_THAN_EQUAL, ">=");
            } else {
                position++;
                return new Token(TokenType.GREATER_THAN, ">");
            }
        } else if (currentChar == '=') {
            if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                position +=2;
                return new Token(TokenType.EQUAL_EQUAL, "==");
            } else {
                
                return new Token(TokenType.ASSIGNMENT, "=");
            }
        } else if (currentChar == '!') {
            if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                position += 2;
                return new Token(TokenType.NOT_EQUAL, "!=");
            } else {
                throw new RuntimeException("Unexpected single '!' character. Did you mean '!='?");
            }
        }
        if (currentChar == '&') {
            if (position < input.length() && input.charAt(position) == '&') {
                position++;
                return new Token(TokenType.LOGICAL_AND, "&&");
            } else {
                return new Token(TokenType.BITWISE_AND, "&");
            }
        } else if (currentChar == '|') {
            if (position < input.length() && input.charAt(position) == '|') {
                position++;
                return new Token(TokenType.LOGICAL_OR, "||");
            } else {
                return new Token(TokenType.BITWISE_OR, "|");
            }
        } else if (currentChar == '~') {
            return new Token(TokenType.BITWISE_NOT, "~");
        } else if (currentChar == '^') {
            return new Token(TokenType.BITWISE_XOR, "^");
        }
        
        position++;
        return new Token(TokenType.OPERATOR, String.valueOf(currentChar));
    }
}
