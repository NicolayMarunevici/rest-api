package com.springboot.blog;

import com.springboot.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootBlogRestApiApplication {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
  }

  @Autowired
  private PostRepository postRepository;

//  @Override
//  public void run(String... args) throws Exception {
//    Post post = new Post();
//    post.setContent("Post Run");
//    post.setTitle("Post");
//    post.setDescription("Comment Run description");
//    postRepository.save(post);
//  }
}
