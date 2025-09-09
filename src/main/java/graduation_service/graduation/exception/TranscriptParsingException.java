package graduation_service.graduation.exception;

public class TranscriptParsingException extends RuntimeException{

    public TranscriptParsingException(String message) {
        super(message);
    }

    public TranscriptParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
