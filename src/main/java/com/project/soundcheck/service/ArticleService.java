package com.project.soundcheck.service;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.Article;

import java.time.LocalDateTime;

public interface ArticleService {
    Response getAllArticles();

    Response getArticleById(Long id);

    Response createArticle(ArticleDTO articleDTO);

    Response updateArticle(Long id, ArticleDTO articleDTO);

    Response deleteArticle(Long articleId);

    Response getArticlesByCarModelId(Long carModelId);
}
