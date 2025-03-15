package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.repo.ArticleRepository;
import com.project.soundcheck.service.ArticleService;
import com.project.soundcheck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Response getAllArticles() {
        Response response = new Response();

        try {
            List<Article> articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<ArticleDTO> articleDTOS = Utils.mapArticleListToArticleDTOList(articles);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setArticleList(articleDTOS);
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error getting all the articles " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getArticleById(Long id) {
        return null;
    }

    @Override
    public Response createArticle(String title, String content, LocalDateTime publishedAt) {
        return null;
    }

    @Override
    public Response updateArticle(Long id, String title, String content, LocalDateTime publishedAt) {
        return null;
    }

    @Override
    public Response deleteArticle(Long articleId) {
        return null;
    }

    @Override
    public Response getArticlesByCarModelId(Long carModelId) {
        return null;
    }
}
