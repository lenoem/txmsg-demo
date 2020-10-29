package com.linewell.msgdemo.mq;

import com.linewell.msgdemo.exceptions.ApasBizException;

public interface MqService {
    SendResult syncSendOrderly(String destination, String msg, String hashKey);
}
