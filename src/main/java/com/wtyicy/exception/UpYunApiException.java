package com.wtyicy.exception;

/**
 * @author wtyicy
 *   2020-10-14 10:29
 */
public class UpYunApiException extends GlobalFileException {
    public UpYunApiException() {
        super();
    }

    public UpYunApiException(String message) {
        super(message);
    }

    public UpYunApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpYunApiException(Throwable cause) {
        super(cause);
    }
}
