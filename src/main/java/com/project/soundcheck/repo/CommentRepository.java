package com.project.soundcheck.repo;

import com.project.soundcheck.model.Comment;
import com.project.soundcheck.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);

    List<Comment> findByUser(User user);
}
