package com.jinx.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutExchangeConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout-mode");
    }
    @Bean
    public Queue fanout1Queue(){
        return QueueBuilder.durable("fanout1-queue").build();
    }
    @Bean
    public Queue fanout2Queue(){
        return QueueBuilder.durable("fanout2-queue").build();
    }

    @Bean
    public Binding fanoutBinding1(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,
                                 @Qualifier("fanout1Queue") Queue queue){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    @Bean
    public Binding fanoutBinding2(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,
                                 @Qualifier("fanout2Queue") Queue queue){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
