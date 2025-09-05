package com.project.soundcheck;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.repo.ArticleRepository;
import com.project.soundcheck.repo.CarModelRepository;
import com.project.soundcheck.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.soundcheck.utils.Utils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CarModelRepository carModelRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Captor
    private ArgumentCaptor<Sort> sortCaptor;

    private ArticleDTO articleDTO;
    private CarModelDTO carModelDTO;
    private CarModel carModel;
    private Article article;
    private Article article1;
    private Article article2;
    private Article savedArticle;
    private ArticleDTO mappedArticleDTO;

    @BeforeEach
    void setUp() {
        // Setup CarModelDTO
        carModelDTO = new CarModelDTO();
        carModelDTO.setId(1L);

        // Setup ArticleDTO
        articleDTO = new ArticleDTO();
        articleDTO.setTitle("Test Article Title");
        articleDTO.setContent("Test article content");
        articleDTO.setPublishedAt(LocalDateTime.now());
        articleDTO.setCarModelDTO(carModelDTO);

        // Setup CarModel entity
        carModel = new CarModel();
        carModel.setId(1L);
        carModel.setModel("Toyota Camry");

        // Setup Article entity
        article = new Article();
        article.setTitle("Test Article Title");
        article.setContent("Test article content");
        article.setPublishedAt(articleDTO.getPublishedAt());
        article.setCarModel(carModel);

        // Setup saved Article (with ID)
        savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setTitle("Test Article Title");
        savedArticle.setContent("Test article content");
        savedArticle.setPublishedAt(articleDTO.getPublishedAt());
        savedArticle.setCarModel(carModel);

        // Setup mapped ArticleDTO
        mappedArticleDTO = new ArticleDTO();
        mappedArticleDTO.setId(1L);
        mappedArticleDTO.setTitle("Test Article Title");
        mappedArticleDTO.setContent("Test article content");
        mappedArticleDTO.setPublishedAt(articleDTO.getPublishedAt());
        mappedArticleDTO.setCarModelDTO(carModelDTO);

        article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Exhaust for BMW");
        article1.setContent("Content A");
        article1.setPublishedAt(LocalDateTime.now());

        article2 = new Article();
        article2.setId(2L);
        article2.setTitle("ECU tuning");
        article2.setContent("Content B");
        article2.setPublishedAt(LocalDateTime.now());

    }

    @Test
    void getAllArticles_returns200_andList() {
        List<Article> articlesFromRepo = List.of(article1, article2);
        when(articleRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(articlesFromRepo);


        CarModelDTO carModelDTO1 = new CarModelDTO();
        carModelDTO1.setId(1L);
        mappedArticleDTO.setCarModelDTO(carModelDTO1);

        ArticleDTO mappedArticleDTO2 = new ArticleDTO();
        mappedArticleDTO2.setId(2L);
        mappedArticleDTO2.setTitle("ECU tuning");
        mappedArticleDTO2.setContent("Content B");

        List<ArticleDTO> expectedDtoList = List.of(mappedArticleDTO, mappedArticleDTO2);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapArticleListToArticleDTOList(articlesFromRepo))
                    .thenReturn(expectedDtoList);


            Response response = articleService.getAllArticles();
            List<ArticleDTO> result = response.getArticleList();

            assertEquals(200, response.getStatusCode());
            assertEquals("Successful", response.getMessage());
            assertEquals(2, result.size(), "The returned list should have two articles.");
            assertEquals(expectedDtoList, result);

            verify(articleRepository, times(1)).findAll(sortCaptor.capture());
            Sort capturedSort = sortCaptor.getValue();
            assertNotNull(capturedSort);
            Sort.Order idOrder = capturedSort.getOrderFor("id");
            assertNotNull(idOrder);
            assertEquals(Sort.Direction.DESC, idOrder.getDirection());
        }
    }


    @Test
    void getArticleById_success() {
        // Arrange
        // Mock the repository to return our test article
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article1));

        // Create the expected DTO that the mocked Utils method should return
        ArticleDTO expectedDto = new ArticleDTO();
        expectedDto.setId(1L);
        expectedDto.setTitle("Exhaust for BMW");
        expectedDto.setContent("Content A");

        // Mock the static call to the Utils class
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapArticleToArticleDTO(article1))
                    .thenReturn(expectedDto);

            // Act
            Response res = articleService.getArticleById(1L);

            // Assert
            assertThat(res.getStatusCode()).isEqualTo(200);
            assertThat(res.getArticleDTO()).isNotNull();
            assertThat(res.getArticleDTO()).isEqualTo(expectedDto); // Assert the returned DTO is what we expect
            assertThat(res.getArticleDTO().getId()).isEqualTo(1L);
            assertThat(res.getArticleDTO().getTitle()).isEqualTo("Exhaust for BMW");
        }

        // Verify that the repository method was indeed called
        verify(articleRepository).findById(1L);
    }

    @Test
    void getArticleById_notFound() {
        when(articleRepository.findById(99L)).thenReturn(Optional.empty());

        Response response = articleService.getArticleById(99L);

        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getMessage()).containsIgnoringCase("not found");
    }

    @Test
    void createArticle_success() {
        // Arrange
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(carModel));
        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapArticleToArticleDTO(savedArticle))
                    .thenReturn(mappedArticleDTO);

            // Act
            Response response = articleService.createArticle(articleDTO);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getArticleDTO()).isNotNull();
            assertThat(response.getArticleDTO().getId()).isEqualTo(mappedArticleDTO.getId());
            assertThat(response.getArticleDTO().getTitle()).isEqualTo(mappedArticleDTO.getTitle());

            // Verify repository calls
            verify(carModelRepository, times(1)).findById(1L);
            verify(articleRepository, times(1)).save(any(Article.class));
            mockedUtils.verify(() -> Utils.mapArticleToArticleDTO(savedArticle), times(1));
        }
    }

    @Test
    void createArticle_carModelNotFound() {
        when(carModelRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = articleService.createArticle(articleDTO);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("Car Model not found");
        assertThat(response.getArticleDTO()).isNull();

        verify(carModelRepository, times(1)).findById(1L);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void updateArticle_success() {
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(1L);
        existingArticle.setTitle("Old Title");
        existingArticle.setContent("Old Content");
        existingArticle.setCarModel(carModel);

        // Updated article DTO
        ArticleDTO updatedArticleDTO = new ArticleDTO();
        updatedArticleDTO.setTitle("Updated Title");
        updatedArticleDTO.setContent("Updated Content");
        updatedArticleDTO.setCarModelDTO(carModelDTO);

        // Article after update
        Article updatedArticle = new Article();
        updatedArticle.setId(1L);
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setContent("Updated Content");
        updatedArticle.setCarModel(carModel);

        // Mock repository responses
        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(carModel));
        when(articleRepository.save(any(Article.class))).thenReturn(updatedArticle);

        // Mock the static Utils method
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapArticleToArticleDTO(updatedArticle))
                    .thenReturn(updatedArticleDTO);

            // Act
            Response response = articleService.updateArticle(1L, updatedArticleDTO);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getArticleDTO()).isNotNull();

            // Verify the updated content
            assertThat(response.getArticleDTO().getTitle()).isEqualTo("Updated Title");
            assertThat(response.getArticleDTO().getContent()).isEqualTo("Updated Content");

            // Verify repository calls and that article was updated with correct values
            verify(articleRepository).findById(1L);
            verify(carModelRepository).findById(1L);
            ArgumentCaptor<Article> articleCaptor = ArgumentCaptor.forClass(Article.class);
            verify(articleRepository).save(articleCaptor.capture());
            
            Article savedArticle = articleCaptor.getValue();
            assertThat(savedArticle.getTitle()).isEqualTo("Updated Title");
            assertThat(savedArticle.getContent()).isEqualTo("Updated Content");
            assertThat(savedArticle.getId()).isEqualTo(1L);
            
            mockedUtils.verify(() -> Utils.mapArticleToArticleDTO(updatedArticle), times(1));
        }
    }

    @Test
    void deleteArticle_success() {
        // Arrange
        Article articleToDelete = new Article();
        articleToDelete.setId(1L);
        articleToDelete.setTitle("Article to Delete");
        articleToDelete.setContent("Content to Delete");
        
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleToDelete));
        doNothing().when(articleRepository).deleteById(1L);

        // Act
        Response response = articleService.deleteArticle(1L);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getStatusCode()).isEqualTo(200);
                assertThat(r.getMessage()).isEqualTo("Successful");
                assertThat(r.getArticleDTO()).isNull();
            });

        // Verify that the article was found and deleted
        verify(articleRepository).findById(1L);
        verify(articleRepository).deleteById(1L);
        
        // Verify no other interactions with the repository
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void deleteArticle_notFound() {
        // Arrange
        Long nonExistentArticleId = 999L;
        when(articleRepository.findById(nonExistentArticleId)).thenReturn(Optional.empty());

        // Act
        Response response = articleService.deleteArticle(nonExistentArticleId);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getStatusCode()).isEqualTo(404);
                assertThat(r.getMessage()).contains("not found");
                assertThat(r.getArticleDTO()).isNull();
            });

        // Verify that we only tried to find the article but didn't delete anything
        verify(articleRepository).findById(nonExistentArticleId);
        verify(articleRepository, never()).delete(any(Article.class));
        
        // Verify no other interactions with the repository
        verifyNoMoreInteractions(articleRepository);
    }
}
