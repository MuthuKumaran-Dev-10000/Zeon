package withrules;

public class ReturnException extends RuntimeException {
    private final Object returnValue;

    public ReturnException(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}

