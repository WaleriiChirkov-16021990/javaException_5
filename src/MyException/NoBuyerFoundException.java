package MyException;

public class NoBuyerFoundException extends Exception{

    public NoBuyerFoundException() {
    }

    public NoBuyerFoundException(String message) {
        super(message);
    }
}
