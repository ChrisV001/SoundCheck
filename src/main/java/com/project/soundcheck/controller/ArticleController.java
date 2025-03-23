package com.project.soundcheck.controller;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllArticles() {
        Response response = articleService.getAllArticles();
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getArticleById(@PathVariable Long id) {
        Response response = articleService.getArticleById(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createArticle(
            @RequestBody ArticleDTO articleDTO
    ) {
        Response response = articleService.createArticle(articleDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        Response response = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteArticle(@PathVariable Long id) {
        Response response = articleService.deleteArticle(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/by-car-model")
    public ResponseEntity<Response> getArticleByCarModelId(@RequestParam Long carModelId) {
        Response response = articleService.getArticlesByCarModelId(carModelId);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
