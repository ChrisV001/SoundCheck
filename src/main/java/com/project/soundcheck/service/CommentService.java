package com.project.soundcheck.service;

import com.project.soundcheck.dto.CommentDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.UserDTO;

public interface CommentService {
    Response getAllComments();

    Response getCommentsByUser(Long userId);

    Response createComment(CommentDTO commentDTO);

    Response updateComment(Long commentId, CommentDTO commentDTO);

    Response deleteComment(Long commentId);
}
