package com.example.sbblogbusiness.events;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestPublishEvent implements Serializable {
  private String message;
  private String title;
  private String content;
  private String userId;
}
