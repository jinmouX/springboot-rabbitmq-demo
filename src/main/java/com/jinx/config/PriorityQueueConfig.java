package com.jinx.config;

import com.jinx.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PriorityQueueConfig {

    @Bean
    public DirectExchange priorityExchange(){
        return new DirectExchange("priority-mode",true,false);
    }

    @Bean
    public Queue priorityQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put(RabbitConstant.QUEUE_MAX_PRIORITY,10);
        return QueueBuilder.durable("priority-queue").withArguments(args).build();
    }
    @Bean
    public Binding priorityBinding(@Qualifier("priorityExchange") DirectExchange exchange,@Qualifier("priorityQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("priority");
    }
}
