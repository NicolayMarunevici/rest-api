package com.springboot.blog.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class PostDto {
  private long id;
  @NotBlank
  @Size(min = 2, message = "Post title should have at least 2 characters")
  private String title;
  @NotBlank
  @Size(min = 10, message = "Post description should have at least 10 characters")
  private String description;
  @NotBlank
  private String content;
  private Set<CommentDto> comments;
}
