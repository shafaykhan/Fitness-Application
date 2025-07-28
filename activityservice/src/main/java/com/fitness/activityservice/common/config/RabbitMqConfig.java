package com.fitness.activityservice.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Bean
  public Queue activityQueue(@Value("${rabbitmq.queue.name}") String name) {
    return new Queue(name, true);
  }

  @Bean
  public DirectExchange activityExchange(@Value("${rabbitmq.exchange.name}") String name) {
    return new DirectExchange(name);
  }

  @Bean
  public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange,
                                 @Value("${rabbitmq.routing.key}") String key) {
    return BindingBuilder.bind(activityQueue).to(activityExchange).with(key);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
