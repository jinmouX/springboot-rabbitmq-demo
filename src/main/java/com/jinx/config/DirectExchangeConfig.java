package com.jinx.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange("direct-mode").durable(true).build();
    }

    @Bean
    public Queue directQueue(){
        return QueueBuilder.durable("direct-queue").build();
    }

    @Bean
    public Binding directBinding(@Qualifier("directExchange") DirectExchange directExchange,
                                 @Qualifier("directQueue") Queue directQueue){
        return BindingBuilder.bind(directQueue).to(directExchange).with("direct");
    }

}
