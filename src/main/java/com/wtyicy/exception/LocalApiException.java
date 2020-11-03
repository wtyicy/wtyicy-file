package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
 */
public class LocalApiException extends GlobalFileException {
    public LocalApiException() {
        super();
    }

    public LocalApiException(String message) {
        super(message);
    }

    public LocalApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalApiException(Throwable cause) {
        super(cause);
    }
}
