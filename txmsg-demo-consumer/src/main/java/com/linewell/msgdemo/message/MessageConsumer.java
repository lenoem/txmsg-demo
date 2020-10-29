package com.linewell.msgdemo.message;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component

@RocketMQMessageListener(consumerGroup = "consumer2",topic = "topic_txmsg")
public class MessageConsumer implements RocketMQListener<String> {

    //接收消息
    @Override
    public void onMessage(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONObject apasInfo = jsonObject.getJSONObject("apasInfo");
        String projid = apasInfo.getString("projid");
        if ("2000".equals(projid)) {
            throw new RuntimeException("人为异常");
        }
        System.out.printf("\n------- consumer2 MessageConsumer received message,\n" +
                        "%s",message);
    }
}
