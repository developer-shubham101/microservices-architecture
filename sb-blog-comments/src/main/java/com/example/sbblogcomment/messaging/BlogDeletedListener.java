package com.example.sbblogcomment.messaging;

import com.example.sbblogcomment.config.RabbitConfig;
import com.example.sbblogcomment.event.BlogDeletedEvent;
import com.example.sbblogcomment.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BlogDeletedListener {
  private final CommentsRepository commentsRepository;

  @RabbitListener(queues = RabbitConfig.DELETED_QUEUE)
  public void onBlogDeleted(BlogDeletedEvent payload) {
    try {
      String blogId = payload.getBlogId();

      if (blogId != null) {
        commentsRepository
            .findByBlogId(blogId)
            .forEach(c -> commentsRepository.deleteById(c.getId()));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
