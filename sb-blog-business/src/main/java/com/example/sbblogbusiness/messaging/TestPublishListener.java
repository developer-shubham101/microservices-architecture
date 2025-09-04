package com.example.sbblogbusiness.messaging;

import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.events.TestPublishEvent;
import com.example.sbblogbusiness.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestPublishListener {
  private final BlogRepository blogRepository;

  @EventListener
  @Async("taskExecutor")
  public void handle(TestPublishEvent event) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    BlogEntity blog = new BlogEntity();
    blog.setTitle(event.getTitle());
    blog.setContent(event.getContent());
    blog.setUserId(event.getUserId());
    blog.setCreatedAt(new java.util.Date());

    try {
      blogRepository.save(blog);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
