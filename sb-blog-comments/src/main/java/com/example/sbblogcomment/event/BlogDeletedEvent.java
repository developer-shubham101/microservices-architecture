package com.example.sbblogcomment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDeletedEvent implements Serializable {
  private String eventId;
  private Instant occurredAt;
  private String blogId;
  private String sourceService = "sb-blog-business";
}
