package com.jinx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  详细重试
 */
@Slf4j
@RestController
public class RetryController {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/retry/{msg}")
    public String retryMessage(@PathVariable("msg")String msg) {
        rabbitTemplate.convertAndSend("retry-mode","retry",msg);
        return "Retry Message 发送成功";
    }


    @RabbitListener(bindings = @QueueBinding(
                    value = @Queue(value = "retry-queue",durable = "true"),
                    exchange = @Exchange(value = "retry-mode",durable = "true",type = "direct"),
                    key = "retry"
            )
    )
    public void retryListener(String msg){
        log.info("[消费者] 收到消息: {}",msg);
        try {
            int i = 10/0;
        } catch (Exception e) {
            log.error("[消费者] 异常:{}", e.getMessage());
            throw e;
        }
    }
}
