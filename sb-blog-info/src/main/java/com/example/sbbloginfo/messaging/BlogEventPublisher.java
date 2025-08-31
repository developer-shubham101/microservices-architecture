package com.example.sbbloginfo.messaging;

import com.example.sbbloginfo.config.RabbitConfig;
import com.example.sbbloginfo.events.BlogCreationFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public void publishBlogCreationFailed(BlogCreationFailedEvent event) {
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.FAILED_ROUTING_KEY, event);
  }
}
