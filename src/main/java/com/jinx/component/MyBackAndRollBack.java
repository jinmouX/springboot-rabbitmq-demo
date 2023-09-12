package com.jinx.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class MyBackAndRollBack implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        //设置发布确认
        rabbitTemplate.setConfirmCallback(this);

        //设置回调
        rabbitTemplate.setMandatory(true);//true 未匹配到队列消息不丢弃
        rabbitTemplate.setReturnCallback(this);
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if(correlationData == null){
            log.info("gggg");
        }else {
            log.info("发送消息:{} ,是否成功:{}, 失败原因{}",new String(correlationData.getReturnedMessage().getBody()),b,s);
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("未能找到指定队列的消息：{}，退回原因{}，交换机{}，路由键：{}",new String(message.getBody()),replyText,exchange,routingKey);
    }
}
