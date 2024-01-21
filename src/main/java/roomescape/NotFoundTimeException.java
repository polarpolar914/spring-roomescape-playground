package roomescape;

public class NotFoundTimeException extends RuntimeException {
    public NotFoundTimeException() {
        super();
    }

    public NotFoundTimeException(String message) {
        super(message);
    }
}
