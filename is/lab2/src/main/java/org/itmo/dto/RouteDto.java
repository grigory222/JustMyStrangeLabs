package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class RouteDto {
    private Integer id;

    private String name;

    private Long coordinatesId;
    private CoordinatesDto coordinates;

    private ZonedDateTime creationDate;

    private Long fromId;
    private LocationDto from;

    private Long toId;
    private LocationDto to;

    private long distance;

    private Long rating;
}



