package Util.exeptions;

public class ConnectionCloseExeption extends RuntimeException {
    public ConnectionCloseExeption(String message) {
        super(message);
    }
    public ConnectionCloseExeption() {super();}
}
