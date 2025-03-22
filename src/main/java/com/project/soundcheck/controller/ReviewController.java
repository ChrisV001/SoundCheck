package com.project.soundcheck.controller;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get-by-exhaustId/{id}")
    public ResponseEntity<Response> getReviewByExhaustSystemId(@PathVariable Long id) {
        Response response = reviewService.getReviewByExhaustSystemId(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/add-review")
    public ResponseEntity<Response> createReview(@RequestBody ReviewDTO reviewDTO) {
        Response response = reviewService.createReview(reviewDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/update-review/{id}")
    public ResponseEntity<Response> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO
    ) {
        Response response = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
