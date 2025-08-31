package com.example.sbblogbusiness.usecase;

import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.dto.BlogRes;
import com.example.sbblogbusiness.dto.CommentRequest;
import com.example.sbblogbusiness.dto.CommentResponse;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.events.BlogCreatedEvent;
import com.example.sbblogbusiness.mapper.BlogMapper;
import com.example.sbblogbusiness.messaging.BlogEventPublisher;
import com.example.sbblogbusiness.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BlogUseCase {
    private final BlogMapper blogMapper; // @RequiredArgsConstructor will create constructor
    private final BlogService blogService;
    private final BlogEventPublisher blogEventPublisher;

    public BlogRes createBlog(BlogReq blogReq) {
        BlogEntity blogEntity = blogMapper.mapToEntity(blogReq);
        BlogEntity newBlogEntity = blogService.createBlog(blogEntity);
        // publish event for Saga choreography
        BlogCreatedEvent event = new BlogCreatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOccurredAt(Instant.now());
        event.setBlogId(newBlogEntity.getId());
        event.setUserId(newBlogEntity.getUserId());
        event.setTitle(newBlogEntity.getTitle());
        event.setContent(newBlogEntity.getContent());
        blogEventPublisher.publishBlogCreated(event);

        return blogMapper.mapToResEntity(newBlogEntity);
    }

    public BlogRes updateBlog(String blogId, BlogReq blogReq) {
        BlogEntity blogEntity = blogMapper.mapToEntity(blogReq);

        BlogEntity newBlogEntity = blogService.updateBlog(blogId, blogEntity);

        return blogMapper.mapToResEntity(newBlogEntity);
    }

    public void deleteBlog(String blogId) {

        blogService.deleteBlog(blogId);
    }

    public CommentResponse createNewComment(String blogId, CommentRequest commentRequest) {

        blogService.getBlogsById(blogId); // If blog exist

        return blogService.createNewComment(blogId, commentRequest);
    }
}
