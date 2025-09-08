package com.example.sbblogcomment.event;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDeletedEvent implements Serializable {
  private String eventId;
  private Instant occurredAt;
  private String blogId;
  private String sourceService = "sb-blog-business";
}
