package com.babel.common.core.exception;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String field = "";

    private String enMsg;
    private String cnMsg;
    private int code;

    public BaseException() {

    }

    /**
     * 自定义的错误信息构造方法
     *
     * @param msg
     */
    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(int code, String enMsg, String cnMsg) {
        super(enMsg);
        this.enMsg = enMsg;
        this.cnMsg = cnMsg;
        this.code = code;
    }

    /**
     * 自定义的错误信息构造方法，带具体栏位
     *
     * @param msg
     */
    public BaseException(String msg, String field) {
        super(msg);
        if (!StringUtils.isEmpty(field)) {
            this.field = field;
        }
    }

    public abstract String getErr();

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public String getCnMsg() {
        return cnMsg;
    }

    public void setEnMsg(String enMsg) {
        this.enMsg = enMsg;
    }

    public void setCnMsg(String cnMsg) {
        this.cnMsg = cnMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
