package by.t1.kotor.clientprocessing.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long clientId) {
        super("Client not found with id: " + clientId);
    }
}
