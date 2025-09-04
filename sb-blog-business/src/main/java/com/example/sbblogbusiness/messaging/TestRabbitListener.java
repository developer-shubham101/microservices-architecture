package com.example.sbblogbusiness.messaging;

import com.example.sbblogbusiness.config.RabbitConfig;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.repository.BlogRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestRabbitListener {
  private final BlogRepository blogRepository;

  @RabbitListener(queues = RabbitConfig.TEST_QUEUE)
  public void onMessage(Object payload) {
    try {
      // payload could be a map or string depending on serialization; keep it simple
      BlogEntity blog = new BlogEntity();
      blog.setTitle("rabbit-test-title");
      blog.setContent(payload != null ? payload.toString() : "payload-null");
      blog.setUserId("rabbit-system");
      blog.setCreatedAt(new Date());
      blogRepository.save(blog);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
