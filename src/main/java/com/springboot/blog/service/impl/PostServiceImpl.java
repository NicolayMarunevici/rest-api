package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;

  private final ModelMapper mapper;

  @Autowired
  public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
    this.postRepository = postRepository;
    this.mapper = mapper;
  }

  @Override
  public PostDto createPost(PostDto postDto) {
    Post post = mapToEntity(postDto);
    Post newPost = postRepository.save(post);

    return mapToDto(newPost);
  }

  @Override
  public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<Post> posts = postRepository.findAll(pageable);
    List<Post> listOfPosts = posts.getContent();
    List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).toList();

    PostResponse postResponse = new PostResponse();
    postResponse.setContent(content);
    postResponse.setPageNo(posts.getNumber());
    postResponse.setPageSize(posts.getSize());
    postResponse.setTotalPages(posts.getTotalPages());
    postResponse.setTotalElements(posts.getTotalElements());
    postResponse.setLast(posts.isLast());

    return postResponse;
  }

  @Override
  public PostDto getPostById(long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    return mapToDto(post);
  }

  @Override
  public PostDto updatePost(PostDto postDto, long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    post.setTitle(postDto.getTitle());
    post.setDescription(postDto.getDescription());
    post.setContent(postDto.getContent());

    Post updatedPost = postRepository.save(post);
    return mapToDto(updatedPost);
  }


  public void deletePost(long id){
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    postRepository.delete(post);
  }

  private PostDto mapToDto(Post post){
    PostDto postDto = mapper.map(post, PostDto.class);

//    PostDto postDto = new PostDto();
//    postDto.setId(post.getId());
//    postDto.setContent(post.getContent());
//    postDto.setTitle(post.getTitle());
//    postDto.setDescription(post.getDescription());
    return postDto;
  }

  private Post mapToEntity(PostDto postDto){
    Post post = mapper.map(postDto, Post.class);

//    Post post = new Post();
//    post.setTitle(postDto.getTitle());
//    post.setDescription(postDto.getDescription());
//    post.setContent(postDto.getContent());
    return post;
  }
}
