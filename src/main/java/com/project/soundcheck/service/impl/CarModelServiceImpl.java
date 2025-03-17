package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.repo.CarModelRepository;
import com.project.soundcheck.service.CarModelService;
import com.project.soundcheck.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;

    @Override
    public Response getAllCarModels() {
        Response response = new Response();

        try {
            List<CarModel> carModels = carModelRepository.findAll();

            List<CarModelDTO> carModelDTOS = Utils.mapCarModelListToCarModelDTOList(carModels);

            response.setCarModelList(carModelDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all car models " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCarModelById(Long id) {
        Response response = new Response();

        try {
            CarModel carModel = carModelRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Car Model not found"));

            CarModelDTO carModelDTO = Utils.mapCarModelToCarModelDTO(carModel);

            response.setCarModelDTO(carModelDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting car model by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createCarModel(String model, Integer year, String engineType, Set<ExhaustSystem> exhaustSystems) {
        Response response = new Response();

        try {
            CarModel carModel = new CarModel();

            carModel.setModel(model);
            carModel.setYear(year);
            carModel.setEngineType(engineType);
            carModel.setExhaustSystems(exhaustSystems);
            CarModel savedCarModel = carModelRepository.save(carModel);

            CarModelDTO carModelDTO = Utils.mapCarModelToCarModelDTO(savedCarModel);

            response.setCarModelDTO(carModelDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating car model " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateCarModel(Long id, String model, Integer year, String engineType, Set<ExhaustSystem> exhaustSystems) {
        Response response = new Response();

        try {
            CarModel carModel = carModelRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Car model not found."));

            if (model != null)
                carModel.setModel(model);

            if (year != null)
                carModel.setYear(year);

            if (engineType != null)
                carModel.setEngineType(engineType);

            if (exhaustSystems != null)
                carModel.setExhaustSystems(exhaustSystems);

            CarModel updateCarModel = carModelRepository.save(carModel);
            CarModelDTO carModelDTO = Utils.mapCarModelToCarModelDTO(updateCarModel);

            response.setCarModelDTO(carModelDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating car model " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCarModel(Long id) {
        Response response = new Response();

        try {
            CarModel carModel = carModelRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Car model not found."));

            carModelRepository.delete(carModel);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting car model " + e.getMessage());
        }
        return response;
    }

}
