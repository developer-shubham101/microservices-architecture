package com.example.sbblogbusiness.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
  public static final String EXCHANGE = "blog.events";
  public static final String CREATED_ROUTING_KEY = "blog.created";
  public static final String FAILED_ROUTING_KEY = "blog.creation.failed";

  @Bean
  public TopicExchange blogExchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Queue blogCreationFailedQueue() {
    return QueueBuilder.durable("sb-blog-business.blog-creation-failed.q").build();
  }

  @Bean
  public Binding bindingBlogCreationFailed(Queue blogCreationFailedQueue, TopicExchange blogExchange) {
    return BindingBuilder.bind(blogCreationFailedQueue).to(blogExchange).with(FAILED_ROUTING_KEY);
  }
}
