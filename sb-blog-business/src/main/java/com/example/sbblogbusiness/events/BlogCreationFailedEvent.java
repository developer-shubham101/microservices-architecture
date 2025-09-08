package com.example.sbblogbusiness.events;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreationFailedEvent implements Serializable {
  private String eventId;
  private Instant occurredAt;
  private String blogId;
  private String reason;
  private String failingService = "sb-blog-info";
}
