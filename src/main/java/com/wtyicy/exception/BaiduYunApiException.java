package com.wtyicy.exception;

/**
 * @author wtyicy
 *   2020-10-14 10:29
 */
public class BaiduYunApiException extends GlobalFileException {
    public BaiduYunApiException() {
        super();
    }

    public BaiduYunApiException(String message) {
        super(message);
    }

    public BaiduYunApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaiduYunApiException(Throwable cause) {
        super(cause);
    }
}
