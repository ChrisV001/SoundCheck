package com.project.soundcheck.service;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;

import java.time.LocalDateTime;

public interface ReviewService {

    Response getReviewByExhaustSystemId(Long exhaustId);

    Response createReview(ReviewDTO reviewDTO);

    Response updateReview(Long id, ReviewDTO reviewDTO);

    Response deleteReview(Long id);
}
