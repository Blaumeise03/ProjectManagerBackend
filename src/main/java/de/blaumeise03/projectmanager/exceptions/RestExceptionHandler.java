package de.blaumeise03.projectmanager.exceptions;

import org.hibernate.TransientPropertyValueException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLSyntaxErrorException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MissingPermissionsException.class)
    protected ResponseEntity<Object> handleMissingPermsEx(MissingPermissionsException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(POJOMappingException.class)
    protected ResponseEntity<Object> handlePOJOMappingException(POJOMappingException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setMessage("An internal error occurred while trying to process the request. Please inform an administrator.");
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    protected ResponseEntity<Object> handleTransientPropertyValueException(TransientPropertyValueException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    protected ResponseEntity<Object> handleSQLSyntaxException(SQLSyntaxErrorException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        //apiError.setMessage(ex.getMessage());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }
}