package withrules;

public enum TokenType {
    IDENTIFIER,
    NUMBER,
    FLOAT,
    STRING,
    OPERATOR,
    COMMENT,
    FOR_LOOP_START,
    FOR_LOOP_END,
    LESS_THAN,       
    GREATER_THAN,    
    LESS_THAN_EQUAL, 
    GREATER_THAN_EQUAL, 
    EQUAL_EQUAL,     
    NOT_EQUAL,       
    LOGICAL_NOT,     
    LOGICAL_AND,     
    LOGICAL_OR,      
    IF,
    ELSE,
    ELSE_IF,
    TRUE,
    FALSE,
    BITWISE_AND,     
    BITWISE_OR,      
    BITWISE_XOR,     
    BITWISE_NOT,     
    FOR, 
    INPUT,  
    WHILE,
    ASSIGNMENT,
    FUNCTION,  
RETURN ,   
TYPE,  

LIST_OPEN,     
LIST_CLOSE,    
TUPLE_OPEN,    
TUPLE_CLOSE,   
DICT_OPEN,     
DICT_CLOSE,    
COLON,         
COMMA,         

CAST_LIST,   
CAST_STRING, 
CAST_NUMBER, 
CAST_FLOAT,   
JAVA_BLOCK,   
PYTHON_BLOCK , 
SEND,  
DOT,
CLASS,
THIS,
NEW,
TRY,        // try
CATCH,      // catch
FINALLY,    // finally
WITH, 
AS,       // 'with' keyword
FILE_MODE,   // 'r', 'w', 'x' (file modes)
WRITE,       // 'write' function
SWITCH,      // switch
    CASE,        // case
    DEFAULT, 
    WEB_BLOCK,
}
