package exception;

public class FileReadFailedException extends RuntimeException {

    public FileReadFailedException(String path) {
        super("Failed to read file. path: " + path);
    }
}
