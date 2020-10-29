package com.linewell.msgdemo.message;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * MessageExtConsumer, consume listener impl class.
 */
@Service
@RocketMQMessageListener(topic = "topic_txmsg", consumerGroup = "consumer1",selectorExpression = "accept")
public class MessageExtConsumer implements RocketMQListener<MessageExt>{
    @Override
    public void onMessage(MessageExt message) {
        System.out.printf("\n------- consumer1 MessageExtConsumer received message,\n" +
                        "QueueId: %s , msgId: %s,tags: %s, \nbody:%s \n",
                message.getQueueId(), message.getMsgId(),message.getTags(), new String(message.getBody()));
    }

}
