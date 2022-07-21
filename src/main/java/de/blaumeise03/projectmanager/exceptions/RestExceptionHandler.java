package de.blaumeise03.projectmanager.exceptions;

import org.hibernate.PropertyValueException;
import org.hibernate.TransientPropertyValueException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    @ExceptionHandler({
            EntityNotFoundException.class,
            PropertyValueException.class,
            MissingPermissionsException.class,
            POJOMappingException.class,
            NullPointerException.class,
            TransientPropertyValueException.class,
            SQLSyntaxErrorException.class,
            NumberFormatException.class,
            IllegalArgumentException.class,
            CookieTheftException.class
    })
    protected ResponseEntity<Object> handleDefaultException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        if (request instanceof ServletWebRequest) {
            ServletWebRequest webRequest = (ServletWebRequest) request;
            apiError.setPath(webRequest.getHttpMethod() + ": " + webRequest.getRequest().getRequestURL().toString());
        } else {
            apiError.setPath(request.getDescription(false));
        }
        Principal user = request.getUserPrincipal();
        if(user != null)
            apiError.setUser(user.getName());
        else apiError.setUser("N/A");
        String session = request.getSessionId();
        int i = Math.max(session.length() - 8, 0);
        apiError.setSession(session.substring(0, i) + "********");
        if (ex instanceof EntityNotFoundException) {
            apiError.setStatus(HttpStatus.NOT_FOUND);
            apiError.setMessage("Requested resource was not found");
        } else if (ex instanceof PropertyValueException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
            apiError.setMessage("Request content is malformed");
        } else if (ex instanceof NumberFormatException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof IllegalArgumentException)  {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof MissingPermissionsException) {
            apiError.setStatus(HttpStatus.FORBIDDEN);
        } else if (ex instanceof POJOMappingException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiError.setMessage(
                    "An internal error occurred while trying to process the request. Please inform an administrator."
            );
        } else if (ex instanceof NullPointerException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof SQLSyntaxErrorException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof CookieTheftException) {
            apiError.setStatus(HttpStatus.UNAUTHORIZED);
        }
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }
}