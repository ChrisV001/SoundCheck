package com.project.soundcheck.controller;

import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.service.CarModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/carmodel")
public class CarModelController {

    private final CarModelService carModelService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCarModels() {
        Response response = carModelService.getAllCarModels();
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCarModelId(@PathVariable Long id) {
        Response response = carModelService.getCarModelById(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createCarModel(
            @RequestBody CarModelDTO carModelDTO
            ) {
        Response response = carModelService.createCarModel(carModelDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Response> updateCarModel(
            @PathVariable Long id,
            @RequestBody CarModelDTO carModelDTO
    ) {
        Response response = carModelService.updateCarModel(id, carModelDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCarModel(@PathVariable Long id) {
        Response response = carModelService.deleteCarModel(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
