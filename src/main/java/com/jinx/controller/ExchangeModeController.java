package com.jinx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *  交换机类型 定向 广播  主题  延迟交换机(插件创建)
 */

@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeModeController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/direct/{msg}")
    public String direct(@PathVariable("msg") String msg){
        log.info("发送direct模式消息到Exchange 消息内容:{}", msg);
        rabbitTemplate.convertAndSend("direct-mode", "direct", msg);
        return  "发送成功！";
    }

    @GetMapping("/fanout/{msg}")
    public String fanout(@PathVariable("msg") String msg){
        log.info("发送fanout模式消息到Exchange 消息内容:{}", msg);
        rabbitTemplate.convertAndSend("fanout-mode", "", msg);
        return  "发送成功！";
    }

    @GetMapping("/topic/{msg}/{level}")
    public String topic(@PathVariable("msg") String msg,@PathVariable("level") String level){
        log.info("发送topic模式消息到Exchange 消息内容:{}", msg);
        rabbitTemplate.convertAndSend("topic-mode", level, msg);
        return  "发送成功！";
    }

    @GetMapping("/delayed/{msg}/{ttl}")
    public String delayed(@PathVariable("msg") String msg,@PathVariable("ttl") Integer time){
        log.info("当前时间:{} 发送延迟消息模式消息到延迟Exchange 消息内容:{} 延迟时间: {}秒", LocalDateTime.now(), msg,time);
        rabbitTemplate.convertAndSend("delayed-mode", "delayed", msg,message -> {
            message.getMessageProperties().setDelay(time * 1000);
            return message;
        });
        return  "发送成功！";
    }
    @GetMapping("/dead/{msg}")
    public String dead(@PathVariable("msg") String msg){
        log.info("当前时间:{} 发送消息模式消息到Exchange 消息内容:{}", LocalDateTime.now(), msg);
        rabbitTemplate.convertAndSend("publi c-mode", "public",msg);
        return  "发送成功！";
    }

    @GetMapping("/backup/{msg}")
    public String backup(@PathVariable("msg") String msg){
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        correlationData.setReturnedMessage(new Message(msg.getBytes()));
        log.info("当前时间:{} 发送消息模式消息到Exchange 消息内容:{}", LocalDateTime.now(), msg);
        rabbitTemplate.convertAndSend("public-mode", "public11",msg,correlationData);
        return  "发送成功！";
    }
}
