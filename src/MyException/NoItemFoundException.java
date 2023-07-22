package MyException;

public class NoItemFoundException extends Exception{
    public NoItemFoundException() {
    }

    public NoItemFoundException(String message) {
        super(message);
    }
}
