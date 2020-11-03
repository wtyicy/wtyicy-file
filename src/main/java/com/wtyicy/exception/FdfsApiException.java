package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
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
