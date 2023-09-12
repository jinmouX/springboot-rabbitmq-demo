package com.jinx.config;

import com.jinx.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeadQueueAndBackupsExchangeConfig {
    @Bean
    public DirectExchange publicExchange() {
        Map<String,Object> args = new HashMap<>();
        args.put(RabbitConstant.EXCHANGE_BACKUPS_NAME,"backup-mode");
        return new DirectExchange("public-mode",true,false,args);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange("dead-mode",true,false);
    }

    @Bean
    public FanoutExchange  backupExchange() {
        return new FanoutExchange("backup-mode",true,false);
    }

    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable("backup-queue").build();
    }
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable("warning-queue").build();
    }

    @Bean
    public Binding backupBinding(@Qualifier("backupExchange") FanoutExchange exchange,@Qualifier("backupQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningBinding(@Qualifier("backupExchange") FanoutExchange exchange,@Qualifier("warningQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }


    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable("dead-queue").build();
    }

    @Bean
    public Queue publicQueue() {
        Map<String,Object> args = new HashMap<>();
        args.put(RabbitConstant.QUEUE_MESSAGE_TTL, 5000);
        args.put(RabbitConstant.DEAD_EXCHANGE_NAME, "dead-mode");
        args.put(RabbitConstant.DEAD_ROUTING_KEY, "dead");
        args.put(RabbitConstant.QUEUE_MAX_LENGTH,6);
        return QueueBuilder.durable("public-queue").withArguments(args).build();
    }
    @Bean
    public Binding deadBinding(@Qualifier("deadQueue") Queue queue, @Qualifier("deadExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dead");
    }


    @Bean
    public Binding publicBinding(@Qualifier("publicExchange") DirectExchange publicExchange,
                                 @Qualifier("publicQueue") Queue publicQueue) {
        return BindingBuilder.bind(publicQueue).to(publicExchange).with("public");
    }
}
