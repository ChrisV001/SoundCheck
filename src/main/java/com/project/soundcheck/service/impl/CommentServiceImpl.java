package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.CommentDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.model.Comment;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ArticleRepository;
import com.project.soundcheck.repo.CommentRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.CommentService;
import com.project.soundcheck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    @Override
    public Response getAllComments() {
        Response response = new Response();

        try {
            List<Comment> commentList = commentRepository.findAll();

            List<CommentDTO> commentDTOS = Utils.mapCommentListToCommentDTOList(commentList);

            response.setCommentList(commentDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all comments: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCommentsByUser(Long userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found!"));

            List<Comment> comments = commentRepository.findByUser(user);

            List<CommentDTO> commentDTOS = Utils.mapCommentListToCommentDTOList(comments);

            response.setCommentList(commentDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting comments by user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createComment(CommentDTO commentDTO) {
        Response response = new Response();

        try {
            Comment comment = new Comment();

            Article article = articleRepository.findById(commentDTO.getArticleDTO().getId())
                    .orElseThrow(() -> new CustomException("Article not found!"));

            User user = userRepository.findById(commentDTO.getUserDTO().getId())
                    .orElseThrow(() -> new CustomException("User not found!"));

            comment.setUser(user);
            comment.setArticle(article);
            comment.setContent(commentDTO.getContent());

            Comment savedComment = commentRepository.save(comment);
            CommentDTO updatedComment = Utils.mapCommentToCommentDTO(savedComment);

            response.setCommentDTO(updatedComment);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating a new comment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateComment(Long commentId, CommentDTO commentDTO) {
        Response response = new Response();

        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found!"));

            if (commentDTO.getId() != null)
                comment.setId(commentDTO.getId());

            if (commentDTO.getArticleDTO() != null) {
                Article article = articleRepository.findById(commentDTO.getArticleDTO().getId())
                        .orElseThrow(() -> new CustomException("Article not found!"));
                comment.setArticle(article);
            }

            if (commentDTO.getUserDTO() != null) {
                User user = userRepository.findById(commentDTO.getUserDTO().getId())
                        .orElseThrow(() -> new CustomException("User not found!"));
                comment.setUser(user);
            }

            if (commentDTO.getContent() != null)
                comment.setContent(commentDTO.getContent());

            // We do not add createdAt since it can be created once and never updated.

            Comment savedComment = commentRepository.save(comment);
            CommentDTO updatedComment = Utils.mapCommentToCommentDTO(savedComment);

            response.setCommentDTO(updatedComment);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating comment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteComment(Long commentId) {
        Response response = new Response();

        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found!"));

            commentRepository.delete(comment);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting comment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response listByArticlePage(Long articleId, int page, int size) {
        Response response = new Response();
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Comment> pageDesc = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId, pageRequest);

            List<CommentDTO> commentDTOS = Utils.mapCommentListToCommentDTOList(pageDesc.getContent());

            response.setCommentList(commentDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setPage(page);
            response.setSize(size);
            response.setTotalElements(pageDesc.getTotalElements());
            response.setTotalPages(pageDesc.getTotalPages());
            response.setHasNext(pageDesc.hasNext());
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error listing by article: " + e.getMessage());
        }
        return response;
    }
}
