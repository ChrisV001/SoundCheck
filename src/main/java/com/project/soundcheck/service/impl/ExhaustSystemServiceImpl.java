package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.service.ExhaustSystemService;
import com.project.soundcheck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExhaustSystemServiceImpl implements ExhaustSystemService {

    private final ExhaustSystemRepository exhaustSystemRepository;

    @Override
    public Response getAllExhaustSystems() {
        Response response = new Response();

        try {
            Set<ExhaustSystem> exhaustSystems = new HashSet<>(exhaustSystemRepository.findAll());

            Set<ExhaustSystemDTO> exhaustSystemDTOS = Utils.mapExhaustSystemListToExhaustSystemDTOList(exhaustSystems);

            response.setExhaustSystemList((List<ExhaustSystemDTO>) exhaustSystemDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all exhaust systems " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getExhaustSystemById(Long id) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Exhaust System id not found"));

            ExhaustSystemDTO exhaustSystemDTO = Utils.mapExhaustSystemToExhaustSystemDTO(exhaustSystem);

            response.setExhaustSystemDTO(exhaustSystemDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting exhaust system by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createExhaustSystem(ExhaustSystemDTO exhaustSystemDTO) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = new ExhaustSystem();

            exhaustSystem.setName(exhaustSystemDTO.getName());
            exhaustSystem.setType(exhaustSystemDTO.getType());
            exhaustSystem.setMaterial(exhaustSystemDTO.getMaterial());
            exhaustSystem.setSoundProfile(exhaustSystemDTO.getSoundProfile());
            exhaustSystem.setPerformanceMetrics(exhaustSystemDTO.getPerformanceMetrics());
            exhaustSystem.setCarModels(Utils.mapCarModelSetToCarModelDTOSet(exhaustSystemDTO.getCarModels()));
            exhaustSystem.setReviews(Utils.mapReviewDTOListToReviewList(exhaustSystemDTO.getReviews()));

            ExhaustSystem savedExhaustSystem = exhaustSystemRepository.save(exhaustSystem);
            ExhaustSystemDTO exhaustSystemDTOSaved = Utils.mapExhaustSystemToExhaustSystemDTO(savedExhaustSystem);

            response.setExhaustSystemDTO(exhaustSystemDTOSaved);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting Exhaust system " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateExhaustSystem(Long id, ExhaustSystemDTO exhaustSystemDTO) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Exhaust system not found"));

            if (exhaustSystemDTO.getId() != null)
                exhaustSystem.setId(exhaustSystemDTO.getId());

            if (exhaustSystemDTO.getName() != null)
                exhaustSystem.setName(exhaustSystemDTO.getName());

            if (exhaustSystemDTO.getType() != null)
                exhaustSystem.setType(exhaustSystemDTO.getType());

            if (exhaustSystemDTO.getMaterial() != null)
                exhaustSystem.setMaterial(exhaustSystemDTO.getMaterial());

            if (exhaustSystemDTO.getSoundProfile() != null)
                exhaustSystem.setSoundProfile(exhaustSystemDTO.getSoundProfile());

            if (exhaustSystemDTO.getPerformanceMetrics() != null)
                exhaustSystem.setPerformanceMetrics(exhaustSystemDTO.getPerformanceMetrics());

            if (exhaustSystemDTO.getCarModels() != null)
                exhaustSystem.setCarModels(exhaustSystem.getCarModels());

            //TODO: Error fetch for null value
            if (exhaustSystemDTO.getReviews() != null)
                exhaustSystem.setReviews(Utils.mapReviewDTOListToReviewList(exhaustSystemDTO.getReviews()));

            ExhaustSystem savedExhaustSystem = exhaustSystemRepository.save(exhaustSystem);

            ExhaustSystemDTO exhaustSystemDTOSaved = Utils.mapExhaustSystemToExhaustSystemDTO(savedExhaustSystem);

            response.setExhaustSystemDTO(exhaustSystemDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating exhaust system " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteExhaustSystem(Long id) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Exhaust System not found"));

            exhaustSystemRepository.delete(exhaustSystem);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting exhaust system " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findExhaustSystemsByType(String type) {
        Response response = new Response();

        try {
            Set<ExhaustSystem> exhaustSystem = new HashSet<>(exhaustSystemRepository.findByType(type));

            Set<ExhaustSystemDTO> exhaustSystemDTOS = Utils.mapExhaustSystemListToExhaustSystemDTOList(exhaustSystem);

            response.setExhaustSystemList((List<ExhaustSystemDTO>) exhaustSystemDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error finding exhaust system by type " + e.getMessage());
        }
        return response;
    }
}
