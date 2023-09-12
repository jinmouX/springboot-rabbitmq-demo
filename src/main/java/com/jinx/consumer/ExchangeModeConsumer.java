package com.jinx.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ExchangeModeConsumer {
    @RabbitListener(queues = "direct-queue")
    public void directListener1(Message message){
      log.info("消费者1获取direct-queue的消息:{}",new String(message.getBody()));
    }
    @RabbitListener(queues = "direct-queue")
    public void directListener2(Message message){
        log.info("消费者2获取direct-queue的消息:{}",new String(message.getBody()));
    }
    @RabbitListener(queues = "fanout1-queue")
    public void fanout1Listener(Message message){
        log.info("获取fanout1-queue的消息:{}",new String(message.getBody()));
    }
    @RabbitListener(queues = "fanout2-queue")
    public void fanout2Listener(Message message){
        log.info("获取fanout2-queue的消息:{}",new String(message.getBody()));
    }

    @RabbitListener(queues = "topic-queue1")
    public void topicListener1(Message message){
        log.info("获取error级别的消息:{}",new String(message.getBody()));
    }
    @RabbitListener(queues = "topic-queue2")
    public void topicListener2(Message message){
        log.info("获取info级别的消息:{}",new String(message.getBody()));
    }

    @RabbitListener(queues = "delayed-queue")
    public void delayedListener(Message message){
        log.info("当前时间:{}获取延迟的消息:{}", LocalDateTime.now(),new String(message.getBody()));
    }

    @RabbitListener(queues = "dead-queue")
    public void deadListener(String msg){
        log.info("当前时间:{}获取死信的消息:{}", LocalDateTime.now(),msg);
    }

    @RabbitListener(queues = "warning-queue")
    public void warningListener(String msg){
        log.info("当前时间:{}获取备份交换机的消息:{}", LocalDateTime.now(),msg);
    }
}
