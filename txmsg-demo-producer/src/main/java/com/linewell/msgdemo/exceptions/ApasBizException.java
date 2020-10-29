package com.linewell.msgdemo.exceptions;

/**
 * 业务异常
 */
public class ApasBizException extends RuntimeException {


    /**
     * 消息发送失败
     */
    public static final ApasBizException MQ_SEND_ERROR = new ApasBizException(90040001, "消息发送失败");

    /**
     * 保存失败
     */
    public static final ApasBizException DB_INSERT_0 = new ApasBizException(90040001, "保存失败");

    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public ApasBizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }
}
