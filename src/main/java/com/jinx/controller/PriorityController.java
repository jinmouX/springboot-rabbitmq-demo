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
 *  优先级队列
 */

@Slf4j
@RestController
public class PriorityController
{
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/priority/{msg}/{priority}")
    public String priority(@PathVariable("msg") String msg,@PathVariable("priority") Integer priority){
        rabbitTemplate.convertAndSend("priority-mode","priority",msg,message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        });
        return "success";
    }
    @RabbitListener(queues = "priority-queue")
    public void priorityListener(String msg){
        log.info("[消费者] 收到消息:{}",msg);
    }

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "priority-queue",durable = "true",arguments = {
//                    @Argument(name = RabbitConstant.QUEUE_MAX_PRIORITY,value = "10",type = "java.lang.Integer")
//            }),
//            exchange = @Exchange(value = "priority-mode"),
//            key = "priority"
//    ))
//    public void priorityListener(String msg){
//        log.info("[消费者] 收到消息:{}",msg);
//    }
}
