package withrules;

/**
 * Represents an else-if node in a syntax tree.
 */
public class ElseIfNode {
    private Node condition;  
    private BlockNode ifBody;  

    private ElseIfNode nextElseIfNode;  


    /**
     * Constructs a new ElseIfNode with the given condition, if body, and next else-if node.
     *
     * @param condition The condition to be evaluated.
     * @param ifBody The block of statements to be executed if the condition is true.
     * @param nextElseIfNode The next else-if node in the syntax tree.
     */
    public ElseIfNode(Node condition, BlockNode ifBody, ElseIfNode nextElseIfNode) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.nextElseIfNode = nextElseIfNode;
    }

    /**
     * Sets the next else-if node in the syntax tree.
     *
     * @param nextElseIfNode The next else-if node.
     */
    public void setNextElseIfNode(ElseIfNode nextElseIfNode) {
        this.nextElseIfNode = nextElseIfNode;
    }

    /**
     * Returns the condition of this else-if node.
     *
     * @return The condition.
     */
    public Node getCondition() {
        return condition;
    }

    /**
     * Returns the block of statements to be executed if the condition is true.
     *
     * @return The block of statements.
     */
    public BlockNode getIfBody() {
        return ifBody;
    }

    /**
     * Returns the next else-if node in the syntax tree.
     *
     * @return The next else-if node.
     */
    public ElseIfNode getNextElseIfNode() {
        return nextElseIfNode;
    }
}

