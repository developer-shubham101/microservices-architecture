package com.example.sbblogbusiness.controller;

import com.example.sbblogbusiness.events.TestPublishEvent;
import com.example.sbblogbusiness.messaging.BlogEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestEventController {
  private final ApplicationEventPublisher eventPublisher;
  private final BlogEventPublisher rabbitPublisher;

  @GetMapping("/blogs/v1/business/test-publish")
  public ResponseEntity<String> testPublish(
      @RequestParam(value = "title", required = false, defaultValue = "test-title") String title,
      @RequestParam(value = "content", required = false, defaultValue = "test-content")
          String content) {
    TestPublishEvent event = new TestPublishEvent("test-message", title, content, "system-user");
    eventPublisher.publishEvent(event);
    return ResponseEntity.ok("event-published");
  }

  @GetMapping("/blogs/v1/business/test-rabbit")
  public ResponseEntity<String> testPublishRabbit(
      @RequestParam(value = "msg", required = false, defaultValue = "hello-rabbit") String msg) {
    rabbitPublisher.publishTestEvent(msg);
    return ResponseEntity.ok("rabbit-event-published");
  }
}
