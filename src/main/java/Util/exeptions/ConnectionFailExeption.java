package Util.exeptions;

public class ConnectionFailExeption extends RuntimeException {
    public ConnectionFailExeption(String message) {
        super(message);
    }
    public ConnectionFailExeption() {super();}
}
