package com.basaki.k8s.error.exception;

/**
 * {@code SecurityConfigurationException} exception is thrown if any problem
 * is encountered during security setup.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
public class SecurityConfigurationException extends RuntimeException {

    public SecurityConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
