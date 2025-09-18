package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteCreateDto {
    private String name;

    private CoordinatesCreateDto coordinates;

    private LocationCreateDto from;

    private LocationCreateDto to;

    private long distance;

    private Long rating;
}


