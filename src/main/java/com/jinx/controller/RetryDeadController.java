package com.jinx.controller;

import com.jinx.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息重试死信
 */

@Slf4j
@RestController
public class RetryDeadController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/retry-dead/{msg}")
    public String retryDead(@PathVariable("msg")String msg) {
        rabbitTemplate.convertAndSend("retry-dead-mode","retry-dead",msg);
        return "Retry Message 发送成功";
    }
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "retry-dead-queue",durable = "true",arguments = {
                            @Argument(name = RabbitConstant.DEAD_EXCHANGE_NAME,value = "from-retry-dead-mode"),
                            @Argument(name = RabbitConstant.DEAD_ROUTING_KEY,value = "from-retry"),
                            @Argument(name = RabbitConstant.QUEUE_MESSAGE_TTL,value = "100000", type = "java.lang.Long")
                    }),
                    exchange = @Exchange(value = "retry-dead-mode",durable = "true",type = "direct"),
                    key = "retry-dead"
            )
    )
    public void retryDeadListener(String msg){
        log.info("[消费者] 收到消息: {}",msg);
        try {
            int i = 10/0;
        } catch (Exception e) {
            log.error("[消费者] 异常:{}", e.getMessage());
            throw e;
        }
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "from-retry-dead-queue",durable = "true"),
                    exchange = @Exchange(value = "from-retry-dead-mode",durable = "true",type = "direct"),
                    key = "from-retry"
            )
    )
    public void deadListener(String msg){
        log.info("[死信队列消费者] 收到消息:{}",msg);
    }
}
