package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.soundcheck.model.ExhaustSystem;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {

    private Long id;

    private ExhaustSystem exhaustSystem;

    private LocalDateTime createdAt = LocalDateTime.now();
}
