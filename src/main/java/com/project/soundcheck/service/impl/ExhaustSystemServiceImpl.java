package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.repo.ExhaustSystemRepository;
import com.project.soundcheck.service.ExhaustSystemService;
import com.project.soundcheck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            List<ExhaustSystem> exhaustSystems = exhaustSystemRepository.findAll();

            List<ExhaustSystemDTO> exhaustSystemDTOS = Utils.mapExhaustSystemListToExhaustSystemDTOList(exhaustSystems);

            response.setExhaustSystemList(exhaustSystemDTOS);
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
    public Response createExhaustSystem(String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = new ExhaustSystem();

            exhaustSystem.setName(name);
            exhaustSystem.setType(type);
            exhaustSystem.setMaterial(material);
            exhaustSystem.setSoundProfile(soundProfile);
            exhaustSystem.setPerformanceMetrics(performanceMetrics);
            exhaustSystem.setCarModels(carModels);
            exhaustSystem.setReviews(reviews);

            ExhaustSystem savedExhaustSystem = exhaustSystemRepository.save(exhaustSystem);
            ExhaustSystemDTO exhaustSystemDTO = Utils.mapExhaustSystemToExhaustSystemDTO(savedExhaustSystem);

            response.setExhaustSystemDTO(exhaustSystemDTO);
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
    public Response updateExhaustSystem(Long id, String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews) {
        Response response = new Response();

        try {
            ExhaustSystem exhaustSystem = exhaustSystemRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Exhaust system not found"));

            if (name != null)
                exhaustSystem.setName(name);

            if (type != null)
                exhaustSystem.setType(type);

            if (material != null)
                exhaustSystem.setMaterial(material);

            if (soundProfile != null)
                exhaustSystem.setSoundProfile(soundProfile);

            if (performanceMetrics != null)
                exhaustSystem.setPerformanceMetrics(performanceMetrics);

            if (carModels != null)
                exhaustSystem.setCarModels(carModels);

            if (reviews != null)
                exhaustSystem.setReviews(reviews);

            ExhaustSystem savedExhaustSystem = exhaustSystemRepository.save(exhaustSystem);

            ExhaustSystemDTO exhaustSystemDTO = Utils.mapExhaustSystemToExhaustSystemDTO(savedExhaustSystem);

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
            List<ExhaustSystem> exhaustSystem = exhaustSystemRepository.findByType(type);

            List<ExhaustSystemDTO> exhaustSystemDTOS = Utils.mapExhaustSystemListToExhaustSystemDTOList(exhaustSystem);

            response.setExhaustSystemList(exhaustSystemDTOS);
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
