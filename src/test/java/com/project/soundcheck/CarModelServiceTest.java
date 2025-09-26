package com.project.soundcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.repo.CarModelRepository;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.service.impl.CarModelServiceImpl;
import com.project.soundcheck.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class CarModelServiceTest {
    @Mock
    private CarModelRepository carModelRepository;
    
    @Mock
    private ExhaustSystemRepository exhaustSystemRepository;

    private CarModelServiceImpl carModelService;

    @Captor
    private ArgumentCaptor<CarModel> captor;

    private CarModel carModel;
    private CarModelDTO carModelDTO;
    private ExhaustSystem exhaustSystem;
    private ExhaustSystemDTO exhaustSystemDTO;

    @BeforeEach
    void setUp() {
        carModelService = new CarModelServiceImpl(carModelRepository, exhaustSystemRepository);

        carModel = new CarModel();
        carModelDTO = new CarModelDTO();
        exhaustSystem = new ExhaustSystem();
        exhaustSystemDTO = new ExhaustSystemDTO();

        exhaustSystem.setId(1L);
        exhaustSystem.setName("Exhaust");

        exhaustSystemDTO.setId(1L);
        exhaustSystemDTO.setName("Exhaust");

        carModel.setId(1L);
        carModel.setEngineType("2Liter");
        carModel.setExhaustSystems(Collections.singleton(exhaustSystem));
        carModel.setModel("Mercedes");
        carModel.setYear(2024);

        carModelDTO.setId(1L);
        carModelDTO.setEngineType("2Liter");
        carModelDTO.setExhaustSystems(Collections.singleton(exhaustSystemDTO));
        carModelDTO.setModel("Mercedes");
        carModelDTO.setYear(2024);
    }

    @Test
    void getAllCarModels_return200() {
        List<CarModel> carModelsFromRepo = List.of(carModel);
        List<CarModelDTO> carModelsDTOsFromRepo = List.of(carModelDTO);

        when(carModelRepository.findAll()).thenReturn(carModelsFromRepo);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCarModelListToCarModelDTOList(carModelsFromRepo))
                .thenReturn(carModelsDTOsFromRepo);

            Response response = carModelService.getAllCarModels();
            List<CarModelDTO> result = response.getCarModelList();

            assertNotNull(result);
            assertEquals(1, result.size());
            CarModelDTO carModelDTOresult = result.get(0);
            assertEquals(carModel.getId(), carModelDTOresult.getId());
            assertEquals(carModel.getEngineType(), carModelDTOresult.getEngineType());
            assertThat(carModelDTOresult.getExhaustSystems())
            .extracting("id", "name")
            .containsExactlyElementsOf(carModel.getExhaustSystems().stream()
                .map(e -> tuple(e.getId(), e.getName()))
                .collect(Collectors.toList()));
            assertEquals(carModel.getModel(), carModelDTOresult.getModel());
            assertEquals(carModel.getYear(), carModelDTOresult.getYear());
        }
    }

    @Test
    void getCarModelById_return200() {
        Long id = 1L;
        when(carModelRepository.findById(id)).thenReturn(Optional.of(carModel));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCarModelToCarModelDTO(carModel))
                .thenReturn(carModelDTO);

            Response response = carModelService.getCarModelById(id);

            assertNotNull(response);
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");

            CarModelDTO result = response.getCarModelDTO();
            assertEquals(result.getEngineType(), carModelDTO.getEngineType());
            assertEquals(result.getExhaustSystems(), carModelDTO.getExhaustSystems());
            assertEquals(result.getId(), carModelDTO.getId());
            assertEquals(result.getModel(), carModelDTO.getModel());
            assertEquals(result.getYear(), carModelDTO.getYear());
        }
    }

    @Test
    void createCarModel_return200() {
        when(exhaustSystemRepository.findById(1L)).thenReturn(Optional.of(exhaustSystem));
        when(carModelRepository.save(any(CarModel.class))).thenReturn(carModel);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCarModelToCarModelDTO(carModel))
                .thenReturn(carModelDTO);

            Response response = carModelService.createCarModel(carModelDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCarModelDTO()).isNotNull();
            assertThat(response.getCarModelDTO().getId()).isEqualTo(carModelDTO.getId());
            assertThat(response.getCarModelDTO().getModel()).isEqualTo(carModelDTO.getModel());

            verify(exhaustSystemRepository, times(1)).findById(1L);
            verify(carModelRepository, times(1)).save(any(CarModel.class));
            mockedUtils.verify(() -> Utils.mapCarModelToCarModelDTO(carModel), times(1));
        }
    }

    @Test
    void updateCarModel_return200() {
        CarModelDTO updatedCarModelDTO = new CarModelDTO();
        updatedCarModelDTO.setId(1L);
        updatedCarModelDTO.setEngineType("Updated");
        updatedCarModelDTO.setModel("Model update");
        updatedCarModelDTO.setYear(2000);

        CarModel updatedCarModel = new CarModel();
        updatedCarModel.setId(1L);
        updatedCarModel.setEngineType("Updated");
        updatedCarModel.setModel("Model update");
        updatedCarModel.setYear(2000);

        when(carModelRepository.findById(carModel.getId())).thenReturn(Optional.of(carModel));
        when(carModelRepository.save(any(CarModel.class))).thenReturn(updatedCarModel);
        //when(exhaustSystemRepository.findById(1L)).thenReturn(Optional.of(exhaustSystem));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapCarModelToCarModelDTO(updatedCarModel))
                .thenReturn(updatedCarModelDTO);

            Response response = carModelService.updateCarModel(1L, updatedCarModelDTO);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getCarModelDTO()).isNotNull();
            
            verify(carModelRepository).save(captor.capture());
            CarModel capturedCarModel = captor.getValue();

            assertThat(capturedCarModel.getModel()).isEqualTo("Model update");
            assertThat(capturedCarModel.getYear()).isEqualTo(2000);
            assertThat(capturedCarModel.getEngineType()).isEqualTo("Updated");

            verify(carModelRepository).findById(1L);
            verify(carModelRepository).save(any(CarModel.class));
            mockedUtils.verify(() -> Utils.mapCarModelToCarModelDTO(updatedCarModel));
        }
    }

    @Test
    void deleteCarModel_return200() {
        CarModel carModelToDelete = new CarModel();
        carModelToDelete.setId(2L);
        carModelToDelete.setEngineType("EngineType");
        carModelToDelete.setModel("Model To Delete");

        when(carModelRepository.findById(2L)).thenReturn(Optional.of(carModelToDelete));
        doNothing().when(carModelRepository).delete(any(CarModel.class));

        Response response = carModelService.deleteCarModel(2L);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getStatusCode()).isEqualTo(200);
                assertThat(r.getMessage()).isEqualTo("Successful");
                assertThat(r.getCarModelDTO()).isNull();
            });

        verify(carModelRepository).findById(2L);
        verify(carModelRepository).delete(carModelToDelete);

        verifyNoMoreInteractions(carModelRepository);
    }
}
