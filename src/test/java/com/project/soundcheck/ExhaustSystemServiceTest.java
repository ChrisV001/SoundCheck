package com.project.soundcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.CarModelRepository;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.repo.ReviewRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.impl.ExhaustSystemServiceImpl;
import com.project.soundcheck.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class ExhaustSystemServiceTest {
    @Mock
    private ExhaustSystemRepository exhaustSystemRepository;

    @Mock
    private CarModelRepository carModelRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExhaustSystemServiceImpl exhaustSystemServiceImpl;

    @Captor
    private ArgumentCaptor<ExhaustSystem> exhaustSystemCaptor;

    private ExhaustSystemDTO exhaustSystemDTO;
    private ExhaustSystem exhaustSystem;
    private CarModel carModel;
    private Review review;
    private User user;
    private CarModelDTO carModelDTO;
    private ReviewDTO reviewDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Initialize all objects
        exhaustSystemDTO = new ExhaustSystemDTO();
        exhaustSystem = new ExhaustSystem();
        carModel = new CarModel();
        review = new Review();
        user = new User();
        carModelDTO = new CarModelDTO();
        reviewDTO = new ReviewDTO();
        userDTO = new UserDTO();

        // Set up exhaust system
        exhaustSystem.setId(1L);
        exhaustSystem.setName("Exhaust");
        exhaustSystem.setMaterial("Material");
        exhaustSystem.setPerformanceMetrics("Performance");
        exhaustSystem.setSoundProfile("SoundProfile");
        exhaustSystem.setType("Type");
        exhaustSystem.setCarModels(java.util.Collections.singleton(carModel));
        exhaustSystem.setReviews(java.util.Collections.emptyList());

        // Set up car model
        carModel.setId(1L);
        carModel.setModel("BMW");

        // Set up user
        user.setId(1L);
        user.setEmail("email@gmail.com");
        user.setPassword("password123@");

        // Set up review
        review.setId(1L);
        review.setExhaustSystem(exhaustSystem);
        review.setUser(user);

        // Set up DTOs
        carModelDTO.setId(1L);
        carModelDTO.setModel("BMW");

        exhaustSystemDTO.setId(1L);
        exhaustSystemDTO.setName("Exhaust");
        exhaustSystemDTO.setMaterial("Material");
        exhaustSystemDTO.setPerformanceMetrics("Performance");
        exhaustSystemDTO.setSoundProfile("SoundProfile");
        exhaustSystemDTO.setType("Type");
        exhaustSystemDTO.setCarModels(java.util.Collections.singleton(carModelDTO));

        // Set up UserDTO
        userDTO.setId(1L);
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername(user.getEmail());
    }

    @Test
    void getAllExhaustSystems_return200() {
        // Arrange
        Set<ExhaustSystem> exhaustFromRepo = Set.of(exhaustSystem);
        List<ExhaustSystem> exhaustFromRepoList = List.of(exhaustSystem);
        when(exhaustSystemRepository.findAll()).thenReturn(exhaustFromRepoList);

        // Act
        Set<ExhaustSystemDTO> exhaustSystemDTOs = Set.of(exhaustSystemDTO);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapExhaustSystemListToExhaustSystemDTOList(exhaustFromRepo))
                    .thenReturn(exhaustSystemDTOs);

            Response response = exhaustSystemServiceImpl.getAllExhaustSystems();
            List<ExhaustSystemDTO> result = response.getExhaustSystemList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            ExhaustSystemDTO resultDTO = result.get(0);
            assertEquals(exhaustSystem.getId(), resultDTO.getId());
            assertEquals(exhaustSystem.getName(), resultDTO.getName());
            assertEquals(exhaustSystem.getMaterial(), resultDTO.getMaterial());
            assertEquals(exhaustSystem.getPerformanceMetrics(), resultDTO.getPerformanceMetrics());
            assertEquals(exhaustSystem.getSoundProfile(), resultDTO.getSoundProfile());
            assertEquals(1, resultDTO.getCarModels().size());
            assertEquals(exhaustSystem.getType(), resultDTO.getType());
        }
    }

    @Test
    void getExhaustSystemById_return200() {
        Long id = 1L;
        when(exhaustSystemRepository.findById(id))
                .thenReturn(Optional.of(exhaustSystem));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapExhaustSystemToExhaustSystemDTO(exhaustSystem))
                    .thenReturn(exhaustSystemDTO);

            //Act
            Response response = exhaustSystemServiceImpl.getExhaustSystemById(id);

            //Assert
            assertNotNull(response);
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");

            ExhaustSystemDTO resultDTO = response.getExhaustSystemDTO();
            assertNotNull(resultDTO);
            assertEquals(exhaustSystem.getId(), resultDTO.getId());
            assertEquals(exhaustSystem.getName(), resultDTO.getName());
            assertEquals(exhaustSystem.getMaterial(), resultDTO.getMaterial());
            assertEquals(exhaustSystem.getPerformanceMetrics(), resultDTO.getPerformanceMetrics());
            assertEquals(exhaustSystem.getSoundProfile(), resultDTO.getSoundProfile());
            assertEquals(exhaustSystem.getType(), resultDTO.getType());
            assertEquals(1, resultDTO.getCarModels().size());
        }
    }

    @Test
    void createExhaustSystem_return200() {
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(carModel));
        when(exhaustSystemRepository.save(any(ExhaustSystem.class))).thenReturn(exhaustSystem);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapExhaustSystemToExhaustSystemDTO(exhaustSystem))
                .thenReturn(exhaustSystemDTO);
            
            Response response = exhaustSystemServiceImpl.createExhaustSystem(exhaustSystemDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getExhaustSystemDTO()).isNotNull();
            assertThat(response.getExhaustSystemDTO().getId()).isEqualTo(exhaustSystemDTO.getId());
            assertThat(response.getExhaustSystemDTO().getName()).isEqualTo(exhaustSystemDTO.getName());

            verify(carModelRepository, times(1)).findById(1L);
            verify(exhaustSystemRepository, times(1)).save(any(ExhaustSystem.class));
            mockedUtils.verify(() -> Utils.mapExhaustSystemToExhaustSystemDTO(exhaustSystem), times(1));
        }
    }

    @Test
    void updateExhaustSystem_return200() {
        // Arrange
        ExhaustSystem updatedExhaustSystem = new ExhaustSystem();
        updatedExhaustSystem.setId(1L);
        updatedExhaustSystem.setCarModels(Collections.singleton(carModel));
        updatedExhaustSystem.setMaterial("Steel");
        updatedExhaustSystem.setName("Remus");
        updatedExhaustSystem.setPerformanceMetrics("performance");
        updatedExhaustSystem.setType("muffler");
        updatedExhaustSystem.setSoundProfile("normal");

        ExhaustSystemDTO updatedExhaustSystemDTO = new ExhaustSystemDTO();
        updatedExhaustSystemDTO.setId(1L);
        updatedExhaustSystemDTO.setCarModels(Collections.singleton(carModelDTO));
        updatedExhaustSystemDTO.setMaterial("Steel");
        updatedExhaustSystemDTO.setName("Remus");
        updatedExhaustSystemDTO.setPerformanceMetrics("performance");
        updatedExhaustSystemDTO.setType("muffler");
        updatedExhaustSystemDTO.setSoundProfile("normal");

        when(exhaustSystemRepository.findById(1L)).thenReturn(Optional.of(exhaustSystem));
        when(exhaustSystemRepository.save(any(ExhaustSystem.class))).thenReturn(updatedExhaustSystem);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapExhaustSystemToExhaustSystemDTO(updatedExhaustSystem))
                .thenReturn(updatedExhaustSystemDTO);
            
            // Act
            Response response = exhaustSystemServiceImpl.updateExhaustSystem(1L, updatedExhaustSystemDTO);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getExhaustSystemDTO()).isNotNull();

            // Verify the exact object being saved
            verify(exhaustSystemRepository).save(exhaustSystemCaptor.capture());
            ExhaustSystem capturedExhaustSystem = exhaustSystemCaptor.getValue();
            
            assertThat(capturedExhaustSystem.getName()).isEqualTo("Remus");
            assertThat(capturedExhaustSystem.getMaterial()).isEqualTo("Steel");
            assertThat(capturedExhaustSystem.getType()).isEqualTo("muffler");
            assertThat(capturedExhaustSystem.getCarModels()).isEqualTo(Collections.singleton(carModel));

            // Verify repository calls
            verify(exhaustSystemRepository).findById(1L);
            verify(exhaustSystemRepository).save(any(ExhaustSystem.class));
            mockedUtils.verify(() -> Utils.mapExhaustSystemToExhaustSystemDTO(updatedExhaustSystem), times(1));
        }
    }
}
