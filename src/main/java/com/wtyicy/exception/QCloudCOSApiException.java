package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
 */
public class QCloudCOSApiException extends GlobalFileException {
    public QCloudCOSApiException() {
        super();
    }

    public QCloudCOSApiException(String message) {
        super(message);
    }

    public QCloudCOSApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public QCloudCOSApiException(Throwable cause) {
        super(cause);
    }
}
