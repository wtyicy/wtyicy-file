package com.wtyicy.exception;

/**
 * aliyun oss 异常处理
 * @author wtyicy
 * @date 2020-10-14 10:29
 */
public class OssApiException extends GlobalFileException {

    public OssApiException(String message) {
        super(message);
    }

    public OssApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
