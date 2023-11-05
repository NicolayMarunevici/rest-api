package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepository;
  private PostRepository postRepository;
  private ModelMapper mapper;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.mapper = mapper;
  }

  @Override
  public CommentDto createComment(long postId, CommentDto commentDto) {
    Comment comment = mapToEntity(commentDto);

    Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    comment.setPost(post);

    Comment newComment = commentRepository.save(comment);

    return mapToDto(newComment);
  }

  @Override
  public List<CommentDto> getCommentsByPostId(long postId) {
    List<Comment> comments = commentRepository.findByPostId(postId);

    return comments.stream().map(comment -> mapToDto(comment)).toList();
  }

  @Override
  public CommentDto getCommentById(Long postId, Long commentId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentId));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if(!comment.getPost().getId().equals(post.getId())){
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    return mapToDto(comment);
  }

  @Override
  public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    comment.setName(commentDto.getName());
    comment.setEmail(commentDto.getEmail());
    comment.setBody(commentDto.getBody());

    if(!comment.getPost().getId().equals(post.getId())){
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    Comment updateComment = commentRepository.save(comment);
    return mapToDto(updateComment);
  }


  @Override
  public void deleteComment(Long postId, Long commentId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));


    if(!comment.getPost().getId().equals(post.getId())){
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    commentRepository.delete(comment);
  }

  private CommentDto mapToDto(Comment comment) {
    CommentDto commentDto = mapper.map(comment, CommentDto.class);

//    CommentDto commentDto = new CommentDto();
//    commentDto.setId(comment.getId());
//    commentDto.setName(comment.getName());
//    commentDto.setEmail(comment.getEmail());
//    commentDto.setBody(comment.getBody());
    return commentDto;
  }

  private Comment mapToEntity(CommentDto commentDto) {
    Comment comment = mapper.map(commentDto, Comment.class);

//    Comment comment = new Comment();
//    comment.setId(commentDto.getId());
//    comment.setName(commentDto.getName());
//    comment.setEmail(commentDto.getEmail());
//    comment.setBody(commentDto.getBody());
    return comment;
  }
}
