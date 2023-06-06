package shop.storage;

public class StorageFileNotFoundExeption extends StorageExeption{
    public StorageFileNotFoundExeption(String message) {
        super(message);
    }

    public StorageFileNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
