package Exeptions;

public class NoSuchUserExeption extends RuntimeException {
    public NoSuchUserExeption(String message) {
        super(message);
    }
}
