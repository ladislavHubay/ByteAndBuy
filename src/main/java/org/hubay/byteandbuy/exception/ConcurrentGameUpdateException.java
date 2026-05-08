package org.hubay.byteandbuy.exception;

/**
 * Signalizuje koliziu pri sucasnej zmene tej istej hry.
 */
public class ConcurrentGameUpdateException extends RuntimeException {
    public ConcurrentGameUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
