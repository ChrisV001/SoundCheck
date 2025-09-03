package com.project.soundcheck;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.repo.ArticleRepository;
import com.project.soundcheck.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Article article1;
    private Article article2;

    @BeforeEach
    void setUp() {
        article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Exhaust for BMW");
        article1.setContent("Content A");
        article1.setPublishedAt(LocalDateTime.of(2023, 6, 1, 12, 0));

        article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Akrapovic on M240i");
        article2.setContent("Content B");
        article2.setPublishedAt(LocalDateTime.of(2024, 1, 15, 10, 0));

        CarModel cm1 = new CarModel();
        cm1.setId(10L);
        cm1.setModel("BMW M240i");
        cm1.setYear(2023);
        cm1.setEngineType("B58");
        article1.setCarModel(cm1);

        CarModel cm2 = new CarModel();
        cm2.setId(11L);
        cm2.setModel("BMW M2");
        cm2.setYear(2024);
        cm2.setEngineType("S58");
        article2.setCarModel(cm2);

    }

//    @Test
//    void getAllArticles_success() {
//        when(articleRepository.findAll()).thenReturn(List.of(article1, article2));
//
//        Response response = articleService.getAllArticles();
//        System.out.println("Service message" + response.getMessage());
//
//        assertThat(response.getStatusCode()).isEqualTo(200);
//        assertThat(response.getMessage()).isEqualTo("Successful");
//        assertThat(response.getArticleList()).hasSize(2);
//        assertThat(response.getArticleList())
//                .extracting(ArticleDTO::getId)
//                .containsExactlyInAnyOrder(1L, 2L);
//        verify(articleRepository).findAll();
//    }

    @Test
    void getArticleById_success() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article1));

        Response res = articleService.getArticleById(1L);

        assertThat(res.getStatusCode()).isEqualTo(200);
        assertThat(res.getArticleDTO()).isNotNull();
        assertThat(res.getArticleDTO().getId()).isEqualTo(1L);
        assertThat(res.getArticleDTO().getTitle()).isEqualTo("Exhaust for BMW");
        verify(articleRepository).findById(1L);
    }
}
