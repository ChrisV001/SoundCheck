package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.Review;

import java.util.List;
import java.util.Set;

public interface ExhaustSystemService {

    Response getAllExhaustSystems();

    Response getExhaustSystemById(Long id);

    Response createExhaustSystem(String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews);

    Response updateExhaustSystem(Long id, String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews);

    Response deleteExhaustSystem(Long id);

    Response findExhaustSystemsByType(String type);
}
