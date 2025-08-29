package com.project.soundcheck.repo;

import com.project.soundcheck.model.Comment;
import com.project.soundcheck.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);

    Page<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId, Pageable pageable);
}
