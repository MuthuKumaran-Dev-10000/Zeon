package withrules;

public class ReturnNode extends Node {
    private final Node returnValue;

    public ReturnNode(Node returnValue) {
        this.returnValue = returnValue;
    }

    public Node getReturnValue() {
        return returnValue;
    }
}

