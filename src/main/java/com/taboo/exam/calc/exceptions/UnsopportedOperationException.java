package taboo.exam.calc.exceptions;

public class UnsopportedOperationException extends RuntimeException  {
    private String operation;

    public UnsopportedOperationException(String message, String operation) {
        this.operation = operation;
    }

    public String getUnsupportedOperation() {
        return operation;
    }
}
