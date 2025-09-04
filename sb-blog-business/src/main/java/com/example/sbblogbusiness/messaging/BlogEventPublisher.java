package com.example.sbblogbusiness.messaging;

import com.example.sbblogbusiness.config.RabbitConfig;
import com.example.sbblogbusiness.events.BlogCreatedEvent;
import com.example.sbblogbusiness.events.BlogCreationFailedEvent;
import com.example.sbblogbusiness.events.BlogDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public void publishBlogCreated(BlogCreatedEvent event) {
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.CREATED_ROUTING_KEY, event);
  }

  public void publishBlogCreationFailed(BlogCreationFailedEvent event) {
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.FAILED_ROUTING_KEY, event);
  }

  public void publishTestEvent(Object event) {
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.TEST_ROUTING_KEY, event);
  }

  public void publishBlogDeleted(BlogDeletedEvent event) {
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.DELETED_ROUTING_KEY, event);
  }
}
