package org.itmo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteImportDto {
    @NotBlank(message = "Route name cannot be empty")
    private String name;
    
    @NotNull(message = "Coordinates cannot be null")
    private CoordinatesDto coordinates;
    
    @NotNull(message = "From location cannot be null")
    private LocationDto from;
    
    @NotNull(message = "To location cannot be null")
    private LocationDto to;
    
    @NotNull(message = "Distance cannot be null")
    private Long distance;
    
    private Long rating;
}
