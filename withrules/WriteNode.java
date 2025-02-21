package withrules;

import java.io.Serializable;

public class WriteNode extends Node implements Serializable {
    private final Node fileVariable;
    private final Node content;

    public WriteNode(Node fileVariable, Node content) {
        this.fileVariable = fileVariable;
        this.content = content;
    }

    public Node getFileVariable() {
        return fileVariable;
    }

    public Node getContent() {
        return content;
    }
}
