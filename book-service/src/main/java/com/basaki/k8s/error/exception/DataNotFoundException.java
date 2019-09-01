package com.basaki.k8s.error.exception;

/**
 * {@code DataNotFoundException} exception is thrown when no item is found
 * during databsase look up.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }
}
