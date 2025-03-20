package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;

    private String message;

    private String token;

    private String role;

    private String expirationDate;

    private ArticleDTO articleDTO;

    private CarModelDTO carModelDTO;

    private ExhaustSystemDTO exhaustSystemDTO;

    private ReviewDTO reviewDTO;

    private UserDTO userDTO;

    private List<ArticleDTO> articleList;

    private List<CarModelDTO> carModelList;

    private List<ExhaustSystemDTO> exhaustSystemList;

    private List<ReviewDTO> reviewList;

    private List<UserDTO> userList;
}
