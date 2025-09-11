package com.project.soundcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.repo.ReviewRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.impl.ExhaustSystemServiceImpl;
import com.project.soundcheck.service.impl.ReviewServiceImpl;
import com.project.soundcheck.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ExhaustSystemRepository exhaustSystemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;

    private Review review;
    private ReviewDTO reviewDTO;
    private User user;
    private UserDTO userDTO;
    private ExhaustSystem exhaustSystem;
    private ExhaustSystemDTO exhaustSystemDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@test.com");

        exhaustSystem = new ExhaustSystem();
        exhaustSystem.setId(1L);
        exhaustSystem.setName("Test Exhaust");

        exhaustSystemDTO = new ExhaustSystemDTO();
        exhaustSystemDTO.setId(1L);
        exhaustSystemDTO.setName("Test Exhaust");

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setExhaustSystem(exhaustSystem);
        review.setCreatedAt(LocalDateTime.now());

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setUserDTO(userDTO);
        reviewDTO.setExhaustSystem(exhaustSystemDTO);
        reviewDTO.setCreatedAt(review.getCreatedAt());
    }

    @Test
    void getReviewByExhaustSystemId_return200() {
        // Arrange
        Long exhaustId = 1L;
        List<Review> reviews = List.of(review);
        List<ReviewDTO> reviewDTOs = List.of(reviewDTO);

        when(reviewRepository.findByExhaustSystemId(exhaustId)).thenReturn(reviews);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapReviewListToReviewDTOList(reviews))
                .thenReturn(reviewDTOs);
            
            // Act
            Response response = reviewServiceImpl.getReviewByExhaustSystemId(exhaustId);

            //Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getReviewList())
                .isNotNull()
                .hasSize(1)
                .containsExactly(reviewDTO);

            verify(reviewRepository).findByExhaustSystemId(exhaustId);
            mockedUtils.verify(() -> Utils.mapReviewListToReviewDTOList(reviews));
            verifyNoMoreInteractions(reviewRepository);
        }
    }

    @Test
    void createReview_return200() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(exhaustSystemRepository.findById(1L)).thenReturn(Optional.of(exhaustSystem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapReviewToReviewDTO(review))
                .thenReturn(reviewDTO);
            
            Response response = reviewServiceImpl.createReview(reviewDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getReviewDTO()).isNotNull();
            assertThat(response.getReviewDTO().getId()).isEqualTo(reviewDTO.getId());
            assertThat(response.getReviewDTO().getUserDTO()).isEqualTo(reviewDTO.getUserDTO());

            verify(exhaustSystemRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).findById(1L);
            verify(reviewRepository, times(1)).save(any(Review.class));
            mockedUtils.verify(() -> Utils.mapReviewToReviewDTO(review), times(1));
        }
    }
}
