package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
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
