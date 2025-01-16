package net.coderic.core.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>WSFailException class.</p>
 *
 * @author <a href="mailto:despacho@neftaliyagua.com">Neftalí Yagua</a>
 * @version $Id: $Id
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class WSFailException extends RuntimeException {
    /**
     * <p>Constructor for TransactionFailException.</p>
     *
     * @param message a {@link String} object
     */
    public WSFailException(String message) {
        super(message);
    }
}