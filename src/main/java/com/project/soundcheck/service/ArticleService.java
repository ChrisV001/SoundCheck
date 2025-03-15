package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.Article;

import java.time.LocalDateTime;

public interface ArticleService {
    Response getAllArticles();

    Response getArticleById(Long id);

    Response createArticle(String title, String content, LocalDateTime publishedAt);

    Response updateArticle(Long id, String title, String content, LocalDateTime publishedAt);

    Response deleteArticle(Long articleId);

    Response getArticlesByCarModelId(Long carModelId);
}
