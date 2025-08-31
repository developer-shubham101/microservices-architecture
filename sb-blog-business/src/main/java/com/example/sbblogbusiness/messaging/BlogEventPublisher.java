package com.example.sbblogbusiness.messaging;

import com.example.sbblogbusiness.config.RabbitConfig;
import com.example.sbblogbusiness.events.BlogCreatedEvent;
import com.example.sbblogbusiness.events.BlogCreationFailedEvent;
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
}
