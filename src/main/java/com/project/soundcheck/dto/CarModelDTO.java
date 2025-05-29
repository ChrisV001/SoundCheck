package com.project.soundcheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarModelDTO {

    private Long id;

    private String model;

    private Integer year;

    private String engineType;

    private Set<ExhaustSystemDTO> exhaustSystems;
}
