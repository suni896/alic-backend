package com.eduhk.alic.alicbackend.common.exception;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @author FuSu
 * @date 2025/1/15 10:45
 */
@Slf4j
public class BaseException extends RuntimeException {
    private Object[] args;

    private ResultCode resultCode;

    public BaseException() {
    }

    public BaseException(String message, Throwable t) {
        super(message, t);
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public ResultCode getErrorCode() {
        return resultCode;
    }

    public void setErrorCode(ResultCode errorCode) {
        this.resultCode = errorCode;
    }

    public BaseException(ResultCode errorCode, Object... args) {
        this.resultCode = errorCode;
        this.args = args;
    }

    public BaseException(ResultCode errorCode, Throwable t, Object... args) {
        super(t);
        this.resultCode = errorCode;
        this.args = args;
    }

    @Override
    public String getMessage() {
        String message;
        if (resultCode != null) {
            message = MessageFormat.format(resultCode.getMessage(), args);
        } else {
            message = super.getMessage();
        }
        return message;
    }

}
