package com.project.soundcheck.controller;

import com.project.soundcheck.dto.CommentDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.Comment;
import com.project.soundcheck.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllComments() {
        Response response = commentService.getAllComments();
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Response> getCommentsByUser(@PathVariable Long userId) {
        Response response = commentService.getCommentsByUser(userId);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewComment(@RequestBody CommentDTO commentDTO) {
        Response response = commentService.createComment(commentDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateComment(@PathVariable("id") Long id, @RequestBody CommentDTO commentDTO) {
        Response response = commentService.updateComment(id, commentDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteComment(@PathVariable("id") Long id) {
        Response response = commentService.deleteComment(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<Response> listPaged(@PathVariable Long articleId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        Response response = commentService.listByArticlePage(articleId, page, size);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
