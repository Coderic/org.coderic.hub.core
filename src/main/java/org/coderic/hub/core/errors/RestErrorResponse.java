package org.coderic.hub.core.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>RestErrorResponse class.</p>
 *
 * @author <a href="mailto:despacho@neftaliyagua.com">Neftalí Yagua</a>
 * @version $Id: $Id
 */
public class RestErrorResponse {
    private int statusCode;
    private HttpStatus status;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    private List<String> errors;

    /**
     * <p>Constructor for RestErrorResponse.</p>
     *
     * @param status     a {@link org.springframework.http.HttpStatus} object
     * @param statusCode a int
     * @param message    a {@link java.lang.String} object
     * @param errors     a {@link java.util.List} object
     */
    public RestErrorResponse(HttpStatus status, int statusCode, String message, List<String> errors) {
        super();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * <p>Constructor for RestErrorResponse.</p>
     *
     * @param status     a {@link org.springframework.http.HttpStatus} object
     * @param statusCode a int
     * @param message    a {@link java.lang.String} object
     * @param error      a {@link java.lang.String} object
     */
    public RestErrorResponse(HttpStatus status, int statusCode, String message, String error) {
        super();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        errors = Arrays.asList(error);
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link org.springframework.http.HttpStatus} object
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return a {@link java.time.LocalDateTime} object
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMessage() {
        return message;
    }

    /**
     * <p>Getter for the field <code>errors</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getErrors() {
        return errors;
    }
}