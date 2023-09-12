package com.jinx.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {
    @Bean
    public TopicExchange topicExchange() {
        return  new TopicExchange("topic-mode",true,false);
    }
    @Bean
    public Queue topicQueue1(){
        return QueueBuilder.durable("topic-queue1").build();
    }
    @Bean
    public Queue topicQueue2(){
        return QueueBuilder.durable("topic-queue2").build();
    }
    @Bean
    public Binding topicBinding1(@Qualifier("topicQueue1") Queue queue,@Qualifier("topicExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("#.error.#");
    }

    @Bean
    public Binding topicBinding2(@Qualifier("topicQueue2") Queue queue,@Qualifier("topicExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("#.info.#");
    }
}
