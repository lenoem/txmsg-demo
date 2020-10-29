package com.linewell.msgdemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linewell.msgdemo.entity.ApasInfo;
import com.linewell.msgdemo.service.ApasInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RocketMQTransactionListener
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    ApasInfoService apasInfoService;

    /**
     * 事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调
     * @param message
     * @param o
     * @return
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            // 可取消息做处理
//            String messageString = new String((byte[]) message.getPayload());
//            JSONObject jsonObject = JSONObject.parseObject(messageString);
//            String apasInfoString = jsonObject.getString("apasInfo");

            ApasInfo apasInfo = (ApasInfo)o;
            //当返回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
            if(apasInfoService.submitInfo(apasInfo)>0){
                return RocketMQLocalTransactionState.COMMIT;
            }else {
                return RocketMQLocalTransactionState.ROLLBACK;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }


    }

    /**
     * 事务状态回查，查询是否登记成功
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String messageString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(messageString);
        String apasInfoString = jsonObject.getString("apasInfo");
        ApasInfo apasInfo = JSON.parseObject(apasInfoString, ApasInfo.class);
        if (apasInfo == null) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        if(apasInfoService.existProjid(apasInfo.getProjid())){
            return RocketMQLocalTransactionState.COMMIT;
        }else{
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
