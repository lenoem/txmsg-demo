package com.linewell.msgdemo.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linewell.msgdemo.dao.ApasInfoDAO;
import com.linewell.msgdemo.entity.ApasInfo;
import com.linewell.msgdemo.exceptions.ApasBizException;
import com.linewell.msgdemo.mq.MqService;
import com.linewell.msgdemo.mq.SendResult;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class ApasInfoService  {

    @Autowired
    private ApasInfoDAO apasInfoDAO;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private MqService mqService;

    public void sendTransactionSubmitInfo(ApasInfo apasInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apasInfo", apasInfo);
        String jsonString = jsonObject.toJSONString();

        //生成message类型
        String key = apasInfo.getProjid();
        MessageBuilder builder = MessageBuilder.withPayload(jsonString)
                .setHeader(RocketMQHeaders.TAGS, "apas_info||test")
                .setHeader(RocketMQHeaders.KEYS, key);
        Message message = builder.build();

        //发送一条事务消息 执行 ProducerTxmsgListener 返回
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("topic_txmsg", message, apasInfo);
        if (!transactionSendResult.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)) {
            throw ApasBizException.MQ_SEND_ERROR;
        }
    }

    public int submitInfo(ApasInfo apasInfo) {
        return apasInfoDAO.insert(apasInfo);
    }

    /**
     * 判断是否存在申报件
     *
     * @param projid
     * @return
     */
    public boolean existProjid(String projid) {
        ApasInfo apasInfo = apasInfoDAO.selectOne(Wrappers.<ApasInfo>lambdaQuery().eq(ApasInfo::getProjid, projid));
        if (apasInfo == null) {
            return false;
        }
        return true;
    }

    @Transactional
    public ApasInfo syncMsgSubmitInfo(ApasInfo apasInfo) {
        if (this.submitInfo(apasInfo) == 0) {
            throw ApasBizException.DB_INSERT_0;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apasInfo", apasInfo);
        String jsonString = jsonObject.toJSONString();

        SendResult sendResult = mqService.syncSendOrderly("topic_txmsg:accept", jsonString, apasInfo.getProjid());
        if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            throw ApasBizException.MQ_SEND_ERROR;
        }
        System.out.println("发送成功msgId:"+sendResult.getMsgId());
        return apasInfo;
    }

    public List<ApasInfo> list() {
        return apasInfoDAO.selectList(null);
    }
}
