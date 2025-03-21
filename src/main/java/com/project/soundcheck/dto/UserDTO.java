package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.soundcheck.model.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<ReviewDTO> reviews;

}
