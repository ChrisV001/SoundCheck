package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.repo.ReviewRepository;
import com.project.soundcheck.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Response getReviewByExhaustSystemId(Long exhaustId) {
        return null;
    }

    @Override
    public Response createReview() {
        return null;
    }

    @Override
    public Response updateReview(Long id, ExhaustSystem exhaustSystem, LocalDateTime createdAt) {
        return null;
    }

    @Override
    public Response deleteReview(Long id) {
        return null;
    }
}
