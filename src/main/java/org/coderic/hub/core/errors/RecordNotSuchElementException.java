package org.coderic.hub.core.errors;
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