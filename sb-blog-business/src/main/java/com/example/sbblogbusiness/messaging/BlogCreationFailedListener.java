package com.example.sbblogbusiness.messaging;

import com.example.sbblogbusiness.events.BlogCreationFailedEvent;
import com.example.sbblogbusiness.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BlogCreationFailedListener {
  private final BlogRepository blogRepository;

  @RabbitListener(queues = "sb-blog-business.blog-creation-failed.q")
  public void handle(BlogCreationFailedEvent event) {
    blogRepository
        .findById(event.getBlogId())
        .ifPresent(
            blog -> {
              // Prefer marking a status field instead of deleting; here we delete for simplicity
              try {
                blogRepository.deleteById(blog.getId());
              } catch (Exception ex) {
                // swallow or log; real implementation should log and alert
                ex.printStackTrace();
              }
            });
  }
}
