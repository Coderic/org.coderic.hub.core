package net.coderic.core.api.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
    /** {@inheritDoc} */
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * <p>handleCSocketTimeout.</p>
     *
     * @param ex a {@link java.net.SocketTimeoutException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler({ SocketTimeoutException.class })
    public ResponseEntity<Object> handleCSocketTimeout(SocketTimeoutException ex, WebRequest request) {
        String error = "Service unavailable";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({ UnknownHostException.class })
    public ResponseEntity<Object> handleUnknownHostException(UnknownHostException ex, WebRequest request) {
        String error = "Gateway Timeout";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "El servidor remoto no se encuentra disponible. ", error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ NoRouteToHostException.class })
    public ResponseEntity<Object> handleNoRouteToHostException(NoRouteToHostException ex, WebRequest request) {
        String error = "No hay ruta al servidor";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "El servidor remoto no se encuentra disponible. ", error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * <p>handleMethodArgumentTypeMismatch.</p>
     *
     * @param ex a {@link org.springframework.web.method.annotation.MethodArgumentTypeMismatchException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * <p>handleNotFound.</p>
     *
     * @param ex a {@link org.springframework.web.server.ResponseStatusException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFound(ResponseStatusException ex, WebRequest request) {
        String error = "Página no encontrada";

        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * <p>handleRecordNotFound.</p>
     *
     * @param ex a {@link org.springframework.web.server.ResponseStatusException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler(RecordNotSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleRecordNotFound(ResponseStatusException ex, WebRequest request) {
        String error = "Este registro no existe";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), error, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * <p>handleUsernameNotFound.</p>
     *
     * @param ex a {@link org.springframework.web.server.ResponseStatusException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(ResponseStatusException ex, WebRequest request) {
        String error = "Usuario no existe";

        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), error, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * <p>handleBadCredentials.</p>
     *
     * @param ex a {@link org.springframework.security.authentication.BadCredentialsException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        String error = "Acceso denegado";
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), error, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthentication(AuthorizationDeniedException ex, WebRequest request) {
        String error = "Acceso denegado";
        System.out.println(error);
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), error, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorMessage errorDetails = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(AuthorizationServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationServiceException(
            AuthorizationServiceException ex, WebRequest request) {

        System.out.println("Acceso denegado");
        return new ResponseEntity<Object>(
                "Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleAuthorizationServiceException(InvalidBearerTokenException ex, WebRequest request) {
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * <p>handleTransactionFailException.</p>
     *
     * @param ex a {@link TransactionFailException} object
     * @param request a {@link org.springframework.web.context.request.WebRequest} object
     * @return a {@link org.springframework.http.ResponseEntity} object
     */
    @ExceptionHandler(TransactionFailException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleTransactionFailException(TransactionFailException ex, WebRequest request) {
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler(WSFailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleWSFailException(WSFailException ex, WebRequest request) {
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler(OAuth2AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> authenticationException(OAuth2AuthenticationException ex, WebRequest request) {
        RestErrorResponse apiError = new RestErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
