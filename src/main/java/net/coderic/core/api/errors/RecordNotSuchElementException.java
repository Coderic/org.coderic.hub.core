package net.coderic.core.api.errors;
/**
 * <p>RecordNotSuchElementException class.</p>
 *
 * @author <a href="mailto:despacho@neftaliyagua.com">Neftalí Yagua</a>
 * @version $Id: $Id
 */
public class RecordNotSuchElementException extends NoSuchElementFoundException {
    public RecordNotSuchElementException(String message) {
        super(message);
    }
}