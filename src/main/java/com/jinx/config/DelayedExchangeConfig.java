package com.jinx.config;

import com.jinx.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedExchangeConfig {

    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put(RabbitConstant.EXCHANGE_DELAYED_TYPE,"direct");
        return new CustomExchange("delayed-mode", RabbitConstant.EXCHANGE_DELAYED_MESSAGE,true,false,args);
    }

    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.durable("delayed-queue").build();
    }
    @Bean
    public Binding delayedBinding(@Qualifier("delayedExchange")CustomExchange customExchange,
                                  @Qualifier("delayedQueue") Queue queue){
        return BindingBuilder.bind(queue).to(customExchange).with("delayed").noargs();
    }


}
