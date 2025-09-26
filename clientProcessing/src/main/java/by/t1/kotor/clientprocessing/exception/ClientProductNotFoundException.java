package by.t1.kotor.clientprocessing.exception;

public class ClientProductNotFoundException extends RuntimeException {
    public ClientProductNotFoundException(Long id) {
        super("ClientProduct not found with id: " + id);
    }
}
