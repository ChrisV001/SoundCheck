package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.repo.ReviewRepository;
import com.project.soundcheck.repo.UserRepository;
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

    private final ExhaustSystemRepository exhaustSystemRepository;

    private final UserRepository userRepository;

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
    public Response createReview(ReviewDTO reviewDTO) {
        Response response = new Response();

        try {
            Review review = new Review();

            ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(reviewDTO.getExhaustSystem().getId())
                    .orElseThrow(() -> new CustomException("Exhaust System not found"));

            User user = userRepository.findById(reviewDTO.getUserDTO().getId())
                    .orElseThrow(() -> new CustomException("User not found"));

            review.setUser(user);
            review.setCreatedAt(reviewDTO.getCreatedAt());
            review.setExhaustSystem(exhaustSystem);

            Review savedReview = reviewRepository.save(review);
            ReviewDTO savedReviewDTO = Utils.mapReviewToReviewDTO(savedReview);

            response.setReviewDTO(savedReviewDTO);
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
    public Response updateReview(Long id, ReviewDTO reviewDTO) {
        Response response = new Response();

        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Review not found."));

            if (reviewDTO.getExhaustSystem().getId() != null) {
                ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(reviewDTO.getExhaustSystem().getId())
                        .orElseThrow(() -> new CustomException("Exhaust System not found"));
                review.setExhaustSystem(exhaustSystem);
            }

            if (reviewDTO.getUserDTO().getId() != null) {
                User user = userRepository.findById(reviewDTO.getUserDTO().getId())
                        .orElseThrow(() -> new CustomException("User not found"));
                review.setUser(user);
            }

            if (reviewDTO.getCreatedAt() != null)
                review.setCreatedAt(reviewDTO.getCreatedAt());

            Review savedReview = reviewRepository.save(review);
            ReviewDTO updatedReviewDTO = Utils.mapReviewToReviewDTO(savedReview);

            response.setReviewDTO(updatedReviewDTO);
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
