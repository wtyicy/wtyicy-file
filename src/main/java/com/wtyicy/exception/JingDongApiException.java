package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
 */
public class JingDongApiException extends GlobalFileException {
    public JingDongApiException() {
        super();
    }

    public JingDongApiException(String message) {
        super(message);
    }

    public JingDongApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public JingDongApiException(Throwable cause) {
        super(cause);
    }
}
