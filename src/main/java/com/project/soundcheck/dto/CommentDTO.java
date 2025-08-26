package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Long id;

    private ArticleDTO articleDTO;

    private UserDTO userDTO;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;
}
