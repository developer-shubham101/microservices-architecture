package com.example.sbblogcomment.messaging;

import com.example.sbblogcomment.config.RabbitConfig;
import com.example.sbblogcomment.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BlogDeletedListener {
  private final CommentsRepository commentsRepository;

  @RabbitListener(queues = RabbitConfig.DELETED_QUEUE)
  public void onBlogDeleted(Object payload) {
    try {
      String blogId = null;
      if (payload instanceof Map<?,?>) {
        @SuppressWarnings("unchecked")
        Map<String, Object> m = (Map<String, Object>) payload;
        Object idObj = m.get("blogId");
        if (idObj != null) blogId = idObj.toString();
      } else if (payload instanceof String s) {
        // assume whole payload contains blogId or JSON
        // naive attempt to extract blogId from JSON-like string
        if (s.contains("\"blogId\"")) {
          int idx = s.indexOf("\"blogId\"");
          int colon = s.indexOf(':', idx);
          int quote = s.indexOf('"', colon + 1);
          int end = s.indexOf('"', quote + 1);
          if (quote > 0 && end > quote) blogId = s.substring(quote + 1, end);
        } else {
          blogId = s;
        }
      }

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
