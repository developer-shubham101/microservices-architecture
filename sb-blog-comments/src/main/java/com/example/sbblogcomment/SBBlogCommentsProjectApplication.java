package com.example.sbblogcomment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SBBlogCommentsProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SBBlogCommentsProjectApplication.class, args);
  }
}

