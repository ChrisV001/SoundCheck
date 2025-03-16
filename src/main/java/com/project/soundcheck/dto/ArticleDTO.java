package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime publishedAt;

    private CarModelDTO carModelDTO;
}
