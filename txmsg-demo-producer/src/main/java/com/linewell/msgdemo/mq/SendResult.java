package com.linewell.msgdemo.mq;

import org.apache.rocketmq.client.producer.SendStatus;

public class SendResult {
    private SendStatus sendStatus;
    private String msgId;

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
