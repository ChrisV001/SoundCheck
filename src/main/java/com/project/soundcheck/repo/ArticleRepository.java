package com.project.soundcheck.repo;

import com.project.soundcheck.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCarModelId(Long carModelId);
}
