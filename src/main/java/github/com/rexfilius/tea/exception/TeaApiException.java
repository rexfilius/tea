package github.com.rexfilius.tea.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TeaApiException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public TeaApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TeaApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

//    public HttpStatus getStatus() {
//        return status;
//    }
//
//    @Override
//    public String getMessage() {
//        return message;
//    }
}
