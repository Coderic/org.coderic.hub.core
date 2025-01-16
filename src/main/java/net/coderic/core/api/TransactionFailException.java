package net.coderic.core.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class TransactionFailException extends RuntimeException {
    /**
     * <p>Constructor for TransactionFailException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public TransactionFailException(String message) {
        super(message);
    }
}