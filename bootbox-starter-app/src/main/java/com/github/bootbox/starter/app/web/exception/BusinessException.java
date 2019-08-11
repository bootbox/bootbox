package com.github.bootbox.starter.app.web.exception;

import com.github.bootbox.web.exception.BaseException;

public class BusinessException extends BaseException {


    public BusinessException() {
        super();
    }

    public BusinessException(int errorCode) {
        this(errorCode, "");
    }

    public BusinessException(int errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    public BusinessException(int errorCode, String internalCode, String defaultMessage) {
        super(errorCode, internalCode, defaultMessage);
    }

    public BusinessException(Throwable cause, int errorCode, String defaultMessage) {
        super(cause, errorCode, defaultMessage);
    }

    public BusinessException(Throwable cause, int errorCode) {
        super(cause, errorCode);
    }

    public BusinessException(int errorCode, String[] args) {
        super(errorCode, args);
    }

}
