package com.project.soundcheck.repo;

import com.project.soundcheck.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByExhaustSystemId(Long exhaustId);
}
