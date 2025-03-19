package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ReviewRepository;
import com.project.soundcheck.service.ReviewService;
import com.project.soundcheck.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Response getReviewByExhaustSystemId(Long exhaustId) {
        Response response = new Response();

        try {
            List<Review> reviews = reviewRepository.findByExhaustSystemId(exhaustId);

            List<ReviewDTO> reviewDTOS = Utils.mapReviewListToReviewDTOList(reviews);

            response.setReviewList(reviewDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting review by exhaust system id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createReview(ExhaustSystem exhaustSystem, LocalDateTime createdAt, User user) {
        Response response = new Response();

        try {
            Review review = new Review();

            review.setExhaustSystem(exhaustSystem);
            review.setCreatedAt(createdAt);
            review.setUser(user);

            Review newReview = reviewRepository.save(review);
            ReviewDTO reviewDTO = Utils.mapReviewToReviewDTO(newReview);

            response.setReviewDTO(reviewDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating review " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateReview(Long id, ExhaustSystem exhaustSystem, LocalDateTime createdAt, User user) {
        Response response = new Response();

        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Review not found."));

            if (exhaustSystem != null)
                review.setExhaustSystem(exhaustSystem);

            if (createdAt != null)
                review.setCreatedAt(createdAt);

            if (user != null)
                review.setUser(user);

            Review updatedReview = reviewRepository.save(review);
            ReviewDTO reviewDTO = Utils.mapReviewToReviewDTO(updatedReview);

            response.setReviewDTO(reviewDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating review " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteReview(Long id) {
        Response response = new Response();

        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Review not found."));

            reviewRepository.delete(review);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting Review " + e.getMessage());
        }
        return response;
    }
}
