package net.coderic.core.api;
/**
 * <p>RecordNotSuchElementException class.</p>
 *
 * @author <a href="mailto:despacho@neftaliyagua.com">Neftalí Yagua</a>
 * @version $Id: $Id
 */
public class RecordNotSuchElementException extends NoSuchElementFoundException {
    /**
     * <p>Constructor for RecordNotSuchElementException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public RecordNotSuchElementException(String message) {
        super(message);
    }
}