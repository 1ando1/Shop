package shop.storage;

public class StorageExeption extends RuntimeException {
    public StorageExeption(String message) {
        super(message);
    }
    public StorageExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
