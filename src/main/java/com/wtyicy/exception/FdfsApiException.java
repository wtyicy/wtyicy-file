package com.wtyicy.exception;

/**
 * @author wtyicy
 * 2020-10-14 10:29
 */
public class FdfsApiException extends GlobalFileException {
    public FdfsApiException() {
        super();
    }

    public FdfsApiException(String message) {
        super(message);
    }

    public FdfsApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public FdfsApiException(Throwable cause) {
        super(cause);
    }
}
