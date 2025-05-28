package com.project.soundcheck.controller;

import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;
import com.project.soundcheck.service.ExhaustSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/exhaustsystems")
@RequiredArgsConstructor
public class ExhaustSystemController {

    private final ExhaustSystemService exhaustSystemService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllExhaustSystems() {
        Response response = exhaustSystemService.getAllExhaustSystems();
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getExhaustSystemById(@PathVariable Long id) {
        Response response = exhaustSystemService.getExhaustSystemById(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createExhaustSystem(
            @RequestBody ExhaustSystemDTO exhaustSystemDTO
    ) {
        Response response = exhaustSystemService.createExhaustSystem(exhaustSystemDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateExhaustSystem(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String material,
            @RequestParam String soundProfile,
            @RequestParam String performanceMetrics,
            @RequestBody(required = false) Set<CarModel> carModels,
            @RequestBody(required = false) List<Review> reviews
    ) {
        Response response = exhaustSystemService.updateExhaustSystem(id, name, type, material, soundProfile, performanceMetrics, carModels, reviews);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteExhaustSystem(@PathVariable Long id) {
        Response response = exhaustSystemService.deleteExhaustSystem(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/byType")
    public ResponseEntity<Response> findExhaustSystemByType(@RequestParam String type) {
        Response response = exhaustSystemService.findExhaustSystemsByType(type);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
