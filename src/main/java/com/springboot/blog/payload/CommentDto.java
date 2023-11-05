package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
  private long id;
  @NotBlank
  private String name;

  @NotBlank(message = "Email should not be null or empty")
  @Email
  private String email;

  @NotBlank
  @Size(min = 10, message = "Comment body should have at least 10 characters")
  private String body;

}
