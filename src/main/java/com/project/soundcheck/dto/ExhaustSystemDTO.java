package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExhaustSystemDTO {

    private Long id;

    private String name;

    private String type;

    private String material;

    private String soundProfile;

    private String performanceMetrics;

    private Set<CarModelDTO> carModels;

    private List<ReviewDTO> reviews;
}
