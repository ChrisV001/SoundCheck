package com.project.soundcheck.controller;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
