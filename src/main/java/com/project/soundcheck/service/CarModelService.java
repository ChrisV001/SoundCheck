package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;

import java.util.List;
import java.util.Set;


public interface CarModelService {
    Response getAllCarModels();

    Response getCarModelById(Long id);

    Response createCarModel(String model, Integer year, String engineType, Set<ExhaustSystem> exhaustSystems);

    Response updateCarModel(Long id, String model, Integer year, String engineType, Set<ExhaustSystem> exhaustSystems);

    Response deleteCarModel(Long id);
}
