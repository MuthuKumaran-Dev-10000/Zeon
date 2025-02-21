package withrules;
import java.util.Objects;

/**
 * Represents an assignment operation in the abstract syntax tree.
 */
public class AssignmentNode extends Node {
    /**
     * The variable being assigned to.
     */
    private VariableNode variable;

    /**
     * The value being assigned.
     */
    private Node value;

    /**
     * Constructs an assignment node.
     *
     * @param variable The variable being assigned to.
     * @param value The value being assigned.
     */
    public AssignmentNode(VariableNode variable, Node value) {
        this.variable = variable;
        this.value = value;
    }

    /**
     * Returns the variable being assigned to.
     *
     * @return The variable being assigned to.
     */
    public VariableNode getVariable() {
        return variable;
    }

    /**
     * Returns the value being assigned.
     *
     * @return The value being assigned.
     */
    public Node getValue() {
        return value;
    }

    /**
     * Returns a string representation of the assignment node.
     *
     * @return A string representation of the assignment node.
     */
    @Override
    public String toString() {
        return "AssignmentNode{" +
                "variable=" + variable +
                ", value=" + value +
                '}';
    }

    /**
     * Checks if this assignment node is equal to another object.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentNode that = (AssignmentNode) o;
        return Objects.equals(variable, that.variable) &&
                Objects.equals(value, that.value);
    }

    /**
     * Returns the hash code of this assignment node.
     *
     * @return The hash code of this assignment node.
     */
    @Override
    public int hashCode() {
        return Objects.hash(variable, value);
    }
}

