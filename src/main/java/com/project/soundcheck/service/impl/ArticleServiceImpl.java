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
        Response response = new Response();

        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Article not found"));

            ArticleDTO articleDTO = Utils.mapArticleToArticleDTO(article);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setArticleDTO(articleDTO);
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting article by ID " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createArticle(String title, String content, LocalDateTime publishedAt) {
        Response response = new Response();

        try {
            Article article = new Article();

            article.setTitle(title);
            article.setContent(content);
            article.setPublishedAt(publishedAt);

            Article savedArticle = articleRepository.save(article);
            ArticleDTO articleDTO = Utils.mapArticleToArticleDTO(savedArticle);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setArticleDTO(articleDTO);
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating article " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateArticle(Long id, String title, String content, LocalDateTime publishedAt) {
        Response response = new Response();

        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Article not found."));

            if (title != null)
                article.setTitle(title);

            if (content != null)
                article.setContent(content);

            if (publishedAt != null)
                article.setPublishedAt(publishedAt);

            Article savedArticle = articleRepository.save(article);
            ArticleDTO articleDTO = Utils.mapArticleToArticleDTO(savedArticle);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setArticleDTO(articleDTO);
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating article " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteArticle(Long articleId) {
        Response response = new Response();

        try {
            articleRepository.findById(articleId)
                    .orElseThrow(() -> new CustomException("Article not found."));

            articleRepository.deleteById(articleId);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting article " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getArticlesByCarModelId(Long carModelId) {
        Response response = new Response();

        try {
            List<Article> articles = articleRepository.findByCarModelId(carModelId);

            List<ArticleDTO> articleDTOS = Utils.mapArticleListToArticleDTOList(articles);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setArticleList(articleDTOS);
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting articles by CarModelID " + e.getMessage());
        }
        return response;
    }
}
