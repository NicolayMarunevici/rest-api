package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<CommentDto> createCommentDto(@PathVariable long postId, @Valid @RequestBody CommentDto commentDto) {
    return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
  }


  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<List<CommentDto>> getPostById(@PathVariable long postId){
    return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
  }

  @GetMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<CommentDto> getCommentById(@PathVariable long postId, @PathVariable long commentId){
    return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
  }


  @PutMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<CommentDto> updateCommentDto(@PathVariable long postId, @PathVariable long commentId, @Valid @RequestBody CommentDto commentDto) {
    return new ResponseEntity<>(commentService.updateCommentById(postId, commentId, commentDto), HttpStatus.CREATED);
  }

  @DeleteMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<String> deleteCommentDto(@PathVariable long postId, @PathVariable long commentId) {
    commentService.deleteComment(postId, commentId);
    return ResponseEntity.ok(String.format("Comment with id %s was deleted", commentId));
  }
}
