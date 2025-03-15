package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;

import java.time.LocalDateTime;

public interface ReviewService {

    Response getReviewByExhaustSystemId(Long exhaustId);

    Response createReview();

    Response updateReview(Long id, ExhaustSystem exhaustSystem, LocalDateTime createdAt);

    Response deleteReview(Long id);
}
