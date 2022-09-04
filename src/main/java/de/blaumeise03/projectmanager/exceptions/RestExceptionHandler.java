package de.blaumeise03.projectmanager.exceptions;

import org.hibernate.PropertyValueException;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.id.IdentifierGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        logger.warn("Received malformed JSON", ex);
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
            SQLIntegrityConstraintViolationException.class,
            IdentifierGenerationException.class,
            NumberFormatException.class,
            IllegalArgumentException.class,
            DataValidationException.class,
            CookieTheftException.class,
    })
    protected ResponseEntity<Object> handleDefaultException(Exception ex, WebRequest request) {
        //Building response debug body
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

        //Overwriting default error handlers, see ResponseEntityExceptionHandler#handleException
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            apiError.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            apiError.setStatus(HttpStatus.NOT_ACCEPTABLE);
        } else if (ex instanceof MissingPathVariableException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof MissingServletRequestParameterException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ServletRequestBindingException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConversionNotSupportedException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof TypeMismatchException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMessageNotReadableException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMessageNotWritableException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof MethodArgumentNotValidException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof MissingServletRequestPartException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof BindException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof NoHandlerFoundException) {
            apiError.setStatus(HttpStatus.NOT_FOUND);
        } else if (ex instanceof AsyncRequestTimeoutException) {
            apiError.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        } else
            //Custom error handling
        if (ex instanceof EntityNotFoundException) {
            apiError.setStatus(HttpStatus.NOT_FOUND);
            //apiError.setMessage("Requested resource was not found: " + ex.getMessage());
        } else if (ex instanceof PropertyValueException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
            apiError.setMessage("Request content is malformed");
        } else if (ex instanceof NumberFormatException) {
            apiError.setStatus(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof DataValidationException) {
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
        } else if (
                ex instanceof SQLSyntaxErrorException ||
                ex instanceof SQLIntegrityConstraintViolationException
        ) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiError.setMessage(
                    "An internal error occurred while trying to process the request. Please inform an administrator!"
            );
        } else if (ex instanceof IdentifierGenerationException) {
            apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (ex instanceof CookieTheftException) {
            apiError.setStatus(HttpStatus.UNAUTHORIZED);
        }

        logger.error("An API Error was caught!", ex);
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        return handleDefaultException(ex, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleDefaultException(ex, request);
    }
}