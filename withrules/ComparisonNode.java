package withrules;

/**
 * Represents a comparison node in an expression tree.
 */
public class ComparisonNode extends Node {
    private Node left;
    private Node right;
    private String operator;

    /**
     * Constructs a new comparison node.
     *
     * @param left The left operand of the comparison.
     * @param operator The comparison operator.
     * @param right The right operand of the comparison.
     */
    public ComparisonNode(Node left, String operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Returns the left operand of the comparison.
     *
     * @return The left operand.
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Returns the right operand of the comparison.
     *
     * @return The right operand.
     */
    public Node getRight() {
        return right;
    }

    /**
     * Returns the comparison operator.
     *
     * @return The comparison operator.
     */
    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "ComparisonNode(" + left + " " + operator + " " + right + ")";
    }
}

