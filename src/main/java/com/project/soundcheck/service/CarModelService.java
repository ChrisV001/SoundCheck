package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;

import java.util.List;


public interface CarModelService {
    Response getAllCarModels();

    Response getCarModelById(Long id);

    Response createCarModel(CarModel carModel);

    Response updateCarModel(Long id, CarModel updateCarModel);

    Response deleteCarModel(Long id);
}
