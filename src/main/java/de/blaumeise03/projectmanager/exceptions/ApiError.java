package de.blaumeise03.projectmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now(ZoneOffset.UTC);
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status, ex.getMessage(), ex);
        //this.status = status;
        //this.message = "Unexpected error";
        //this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        if(ex.getMessage() == null || ex.getMessage().equals(ex.getLocalizedMessage()))
            this.debugMessage = ex.getClass().getName() + ": " + ex.getLocalizedMessage();
        else this.debugMessage = ex.getLocalizedMessage();
        while (ex.getCause() != null) {
            if(this.subErrors == null) subErrors = new ArrayList<>();
            ex = ex.getCause();
            this.subErrors.add(new CauseError("Caused by: " + ex.getClass().getName(), ex.getLocalizedMessage()));
        }
    }

    abstract class ApiSubError {

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    class ApiValidationError extends ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        ApiValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    class CauseError extends ApiSubError {
        private String message;
        private String debugMessage;
    }
}
