package com.project.soundcheck.service;

import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;

import java.util.List;
import java.util.Set;


public interface CarModelService {
    Response getAllCarModels();

    Response getCarModelById(Long id);

    Response createCarModel(CarModelDTO carModelDTO);

    Response updateCarModel(Long id, CarModelDTO carModelDTO);

    Response deleteCarModel(Long id);
}
