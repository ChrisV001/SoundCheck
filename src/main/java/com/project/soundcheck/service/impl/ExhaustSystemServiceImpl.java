package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.service.ExhaustSystemService;

import java.util.List;
import java.util.Set;

public class ExhaustSystemServiceImpl implements ExhaustSystemService {
    @Override
    public Response getAllExhaustSystems() {
        return null;
    }

    @Override
    public Response getExhaustSystemById(Long id) {
        return null;
    }

    @Override
    public Response createExhaustSystem(String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews) {
        return null;
    }

    @Override
    public Response updateExhaustSystem(Long id, String name, String type, String material, String soundProfile, String performanceMetrics, Set<CarModel> carModels, List<Review> reviews) {
        return null;
    }

    @Override
    public Response deleteExhaustSystem(Long id) {
        return null;
    }

    @Override
    public Response findExhaustSystemsByType(String type) {
        return null;
    }
}
