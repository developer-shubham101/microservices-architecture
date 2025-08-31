package com.example.sbbloginfo.messaging;
import com.example.sbbloginfo.events.BlogCreatedEvent;
import com.example.sbbloginfo.events.BlogCreationFailedEvent;
import com.example.sbbloginfo.entity.BlogEntity;
import com.example.sbbloginfo.repository.BlogRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BlogCreatedEventListener {
  private final BlogRepository blogRepository;
  private final BlogEventPublisher blogEventPublisher;

  @RabbitListener(queues = "sb-blog-info.blog-created.q")
  @Transactional
  public void handle(BlogCreatedEvent event) {
    // idempotency check
    Optional<BlogEntity> existing = blogRepository.findById(event.getBlogId());
    if (existing.isPresent()) {
      return;
    }

    try {
      BlogEntity info = new BlogEntity();
      info.setId(event.getBlogId());
      info.setUserId(event.getUserId());
      info.setTitle(event.getTitle());
      info.setContent(event.getContent());
      info.setCreatedAt(java.util.Date.from(event.getOccurredAt()));

      blogRepository.save(info);
    } catch (Exception ex) {
      BlogCreationFailedEvent failed = new BlogCreationFailedEvent();
      failed.setEventId(event.getEventId());
      failed.setOccurredAt(Instant.now());
      failed.setBlogId(event.getBlogId());
      failed.setReason("Failed to save aggregated blog info: " + ex.getMessage());
      blogEventPublisher.publishBlogCreationFailed(failed);

      // rethrow to let the broker/consumer retry or send to DLQ as configured
      throw ex;
    }
  }
}
