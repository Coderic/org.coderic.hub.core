package net.coderic.core.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchElementFoundException extends ResponseStatusException {
    /**
     * <p>Constructor for NoSuchElementFoundException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public NoSuchElementFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}