package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class RouteResponseDto {
    private Integer id;
    private String name;

    private CoordinatesResponseDto coordinates;
    private ZonedDateTime creationDate;
    private LocationResponseDto from;
    private LocationResponseDto to;
    private long distance;
    private Long rating;
}


