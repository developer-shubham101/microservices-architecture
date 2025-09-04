package com.example.sbblogcomment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RabbitConfig {
  public static final String EXCHANGE = "blog.events";
  public static final String DELETED_ROUTING_KEY = "blog.deleted";
  public static final String DELETED_QUEUE = "sb-blog-business.blog-deleted.q";

  @Bean
  public TopicExchange blogExchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Queue blogDeletedQueue() {
    return QueueBuilder.durable(DELETED_QUEUE).build();
  }

  @Bean
  public Binding bindingBlogDeleted(Queue blogDeletedQueue, TopicExchange blogExchange) {
    return BindingBuilder.bind(blogDeletedQueue).to(blogExchange).with(DELETED_ROUTING_KEY);
  }
}
