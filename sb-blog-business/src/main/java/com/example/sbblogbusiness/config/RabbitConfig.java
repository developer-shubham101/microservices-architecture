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
  public static final String TEST_ROUTING_KEY = "blog.test.event";
  public static final String TEST_QUEUE = "sb-blog-business.blog-test-event.q";
  public static final String DELETED_ROUTING_KEY = "blog.deleted";
  public static final String DELETED_QUEUE = "sb-blog-business.blog-deleted.q";

  @Bean
  public TopicExchange blogExchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Queue blogCreationFailedQueue() {
    return QueueBuilder.durable("sb-blog-business.blog-creation-failed.q").build();
  }

  @Bean
  public Queue blogTestEventQueue() {
    return QueueBuilder.durable(TEST_QUEUE).build();
  }

  @Bean
  public Binding bindingBlogCreationFailed(
      Queue blogCreationFailedQueue, TopicExchange blogExchange) {
    return BindingBuilder.bind(blogCreationFailedQueue).to(blogExchange).with(FAILED_ROUTING_KEY);
  }

  @Bean
  public Binding bindingBlogTestEvent(Queue blogTestEventQueue, TopicExchange blogExchange) {
    return BindingBuilder.bind(blogTestEventQueue).to(blogExchange).with(TEST_ROUTING_KEY);
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
