package com.project.soundcheck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.CommentDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.model.Comment;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ArticleRepository;
import com.project.soundcheck.repo.CommentRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.impl.CommentServiceImpl;
import com.project.soundcheck.utils.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    private CommentServiceImpl commentServiceImpl;

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    private User user;
    private UserDTO userDTO;
    private Comment comment;
    private CommentDTO commentDTO;
    private Article article;
    private ArticleDTO articleDTO;

    @BeforeEach
    void setUp() {
        commentServiceImpl = new CommentServiceImpl(commentRepository, userRepository, articleRepository);

        // Initialize User and UserDTO
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setUsername("testUser");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testUser");

        article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setContent("Test Content");

        articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        articleDTO.setTitle("Test Article");
        articleDTO.setContent("Test Content");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");
        comment.setUser(user);
        comment.setArticle(article);

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("Test Comment");
        commentDTO.setUserDTO(userDTO);
        commentDTO.setArticleDTO(articleDTO);
        commentDTO.setCreatedAt(comment.getCreatedAt());
    }

    @Test
    void getAllComments_return200() {
        List<Comment> comments = List.of(comment);
        List<CommentDTO> commentDTOs = List.of(commentDTO);

        when(commentRepository.findAll()).thenReturn(comments);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCommentListToCommentDTOList(comments))
                .thenReturn(commentDTOs);

            Response response = commentServiceImpl.getAllComments();
            List<CommentDTO> commentDTOsResponse = response.getCommentList();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCommentList()).isNotNull();

            CommentDTO result = commentDTOsResponse.get(0);
            assertEquals(comment.getId(), result.getId());
            assertEquals(comment.getContent(), result.getContent());
            assertEquals(comment.getCreatedAt(), result.getCreatedAt());
            assertEquals(comment.getUpdatedAt(), result.getUpdatedAt());
        }
    }

    @Test
    void getCommentsByUser_return200() {
        List<Comment> comments = List.of(comment);
        List<CommentDTO> commentDTOs = List.of(commentDTO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.findByUser(user)).thenReturn(comments);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCommentListToCommentDTOList(comments))
                .thenReturn(commentDTOs);

            Response response = commentServiceImpl.getCommentsByUser(user.getId());

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");

            CommentDTO result = response.getCommentList().get(0);
            assertThat(result.getId()).isEqualTo(comment.getId());
            assertThat(result.getContent()).isEqualTo(comment.getContent());
            assertThat(result.getCreatedAt()).isEqualTo(comment.getCreatedAt());

            verify(userRepository).findById(1L);
            verify(commentRepository).findByUser(user);
            verifyNoMoreInteractions(userRepository, commentRepository);
        }
    }

    @Test
    void createComment_return200() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCommentToCommentDTO(comment))
                .thenReturn(commentDTO);

            Response response = commentServiceImpl.createComment(commentDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCommentDTO()).isNotNull();
            assertThat(response.getCommentDTO().getContent()).isEqualTo(commentDTO.getContent());
            assertThat(response.getCommentDTO().getId()).isEqualTo(commentDTO.getId());

            verify(articleRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).findById(1L);
            verify(commentRepository, times(1)).save(any(Comment.class));
            mockedUtils.verify(() -> Utils.mapCommentToCommentDTO(comment), times(1));
        }
    }

    @Test
    void updateComment_return200() {
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setArticle(article);
        updatedComment.setUser(user);
        updatedComment.setContent("Update Content");

        CommentDTO updatedCommentDTO = new CommentDTO();
        updatedCommentDTO.setId(1L);
        updatedCommentDTO.setArticleDTO(articleDTO);
        updatedCommentDTO.setUserDTO(userDTO);
        updatedCommentDTO.setContent("Update Content");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCommentToCommentDTO(updatedComment))
                .thenReturn(updatedCommentDTO);
            
            Response response = commentServiceImpl.updateComment(1L, updatedCommentDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCommentDTO()).isNotNull();
            assertThat(response.getCommentDTO().getContent()).isEqualTo("Update Content");
            assertThat(response.getCommentDTO().getId()).isEqualTo(1L);

            verify(commentRepository).findById(1L);
            verify(articleRepository).findById(1L);
            verify(userRepository).findById(1L);
            verify(commentRepository).save(commentCaptor.capture());

            Comment capturedComment = commentCaptor.getValue();
            assertThat(capturedComment.getContent()).isEqualTo("Update Content");
            assertThat(capturedComment.getId()).isEqualTo(1L);

            mockedUtils.verify(() -> Utils.mapCommentToCommentDTO(updatedComment), times(1));
        }
    }

    @Test
    void deleteComment_return200() {
        Comment commentToDelete = new Comment();
        commentToDelete.setArticle(article);
        commentToDelete.setContent("Content");
        commentToDelete.setUser(user);
        commentToDelete.setId(2L);

        when(commentRepository.findById(2L)).thenReturn(Optional.of(commentToDelete));
        doNothing().when(commentRepository).delete(any(Comment.class));

        Response response = commentServiceImpl.deleteComment(2L);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Successful");
        assertThat(response.getCommentDTO()).isNull();

        verify(commentRepository).findById(2L);
        verify(commentRepository).delete(commentToDelete);

        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void listByArticlePage_return200() {
        Long articleId = 1L;
        int page = 0;
        int size = 5;

        List<Comment> comments = List.of(comment);
        List<CommentDTO> commentDTOs = List.of(commentDTO);

        Page<Comment> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(comments);
        when(mockPage.getTotalElements()).thenReturn(1L);
        when(mockPage.getTotalPages()).thenReturn(1);
        when(mockPage.hasNext()).thenReturn(false);

        when(commentRepository.findByArticleIdOrderByCreatedAtDesc(eq(articleId), any(PageRequest.class)))
            .thenReturn(mockPage);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCommentListToCommentDTOList(comments))
                .thenReturn(commentDTOs);

            Response response = commentServiceImpl.listByArticlePage(articleId, page, size);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCommentList()).isNotNull();
            assertThat(response.getCommentList()).hasSize(1);
            assertThat(response.getPage()).isEqualTo(page);
            assertThat(response.getSize()).isEqualTo(size);
            assertThat(response.getTotalElements()).isEqualTo(1L);
            assertThat(response.getTotalPages()).isEqualTo(1);
            assertThat(response.getHasNext()).isFalse();

            CommentDTO result = response.getCommentList().get(0);
            assertThat(result.getId()).isEqualTo(comment.getId());
            assertThat(result.getContent()).isEqualTo(comment.getContent());
            assertThat(result.getCreatedAt()).isEqualTo(comment.getCreatedAt());

            verify(commentRepository).findByArticleIdOrderByCreatedAtDesc(eq(articleId), any(PageRequest.class));
            mockedUtils.verify(() -> Utils.mapCommentListToCommentDTOList(comments), times(1));

        }
    }
}
