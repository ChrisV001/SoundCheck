package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.repo.CarModelRepository;
import com.project.soundcheck.service.CarModelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;

    public CarModelServiceImpl(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    @Override
    public Response getAllCarModels() {
        return null;
    }

    @Override
    public Response getCarModelById(Long id) {
        return null;
    }

    @Override
    public Response createCarModel(CarModel carModel) {
        return null;
    }

    @Override
    public Response updateCarModel(Long id, CarModel updateCarModel) {
        return null;
    }

    @Override
    public Response deleteCarModel(Long id) {
        return null;
    }
}
