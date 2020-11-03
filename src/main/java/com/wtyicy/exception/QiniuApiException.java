package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
 */
public class QiniuApiException extends GlobalFileException {
    public QiniuApiException() {
        super();
    }

    public QiniuApiException(String message) {
        super(message);
    }

    public QiniuApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public QiniuApiException(Throwable cause) {
        super(cause);
    }
}
