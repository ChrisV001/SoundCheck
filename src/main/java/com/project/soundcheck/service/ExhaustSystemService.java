package com.project.soundcheck.service;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.Review;

import java.util.List;
import java.util.Set;

public interface ExhaustSystemService {

    Response getAllExhaustSystems();

    Response getExhaustSystemById(Long id);

    Response createExhaustSystem(ExhaustSystemDTO exhaustSystemDTO);

    Response updateExhaustSystem(Long id, ExhaustSystemDTO exhaustSystemDTO);

    Response deleteExhaustSystem(Long id);

    Response findExhaustSystemsByType(String type);
}
