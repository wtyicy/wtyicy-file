package com.wtyicy.exception;

/**
 * @author wtyicy
 * @date 2018/12/5 17:15
 */
public class HuaWeiYunApiException extends GlobalFileException {
    public HuaWeiYunApiException() {
        super();
    }

    public HuaWeiYunApiException(String message) {
        super(message);
    }

    public HuaWeiYunApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public HuaWeiYunApiException(Throwable cause) {
        super(cause);
    }
}
