package com.linewell.msgdemo.mq.impl;

import com.linewell.msgdemo.exceptions.ApasBizException;
import com.linewell.msgdemo.mq.MqService;
import com.linewell.msgdemo.mq.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class RocketMqService implements MqService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public SendResult syncSendOrderly(String destination, String msg, String hashKey) {

        if ("1000".equals(hashKey)) {
            System.out.println("人为异常");
            throw ApasBizException.MQ_SEND_ERROR;
        }

        Message<String> message = MessageBuilder.withPayload(msg)
                .setHeader(RocketMQHeaders.KEYS,"key").build();

        org.apache.rocketmq.client.producer.SendResult mqSendResult = rocketMQTemplate.syncSendOrderly(destination,message,hashKey);
        SendResult sendResult = new SendResult();
//        if (!mqSendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
//            throw ApasBizException.MQ_SEND_ERROR;
//        }
        sendResult.setSendStatus(mqSendResult.getSendStatus());
        sendResult.setMsgId(mqSendResult.getMsgId());
        return sendResult;
    }
}
