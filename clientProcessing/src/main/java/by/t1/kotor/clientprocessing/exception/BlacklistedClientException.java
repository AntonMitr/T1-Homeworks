package by.t1.kotor.clientprocessing.exception;

public class BlacklistedClientException extends RuntimeException {
    public BlacklistedClientException(String documentId) {
        super("Client with documentId " + documentId + " is blacklisted");
    }
}
