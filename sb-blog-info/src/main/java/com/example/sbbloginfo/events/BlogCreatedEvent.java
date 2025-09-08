package com.example.sbbloginfo.events;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreatedEvent implements Serializable {
  private String eventId;
  private Instant occurredAt;
  private String blogId;
  private String userId;
  private String title;
  private String content;
  private String sourceService = "sb-blog-business";
}
