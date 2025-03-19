package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;

import java.time.LocalDateTime;

public interface ReviewService {

    Response getReviewByExhaustSystemId(Long exhaustId);

    Response createReview(ExhaustSystem exhaustSystem, LocalDateTime createdAt, User user);

    Response updateReview(Long id, ExhaustSystem exhaustSystem, LocalDateTime createdAt, User user);

    Response deleteReview(Long id);
}
