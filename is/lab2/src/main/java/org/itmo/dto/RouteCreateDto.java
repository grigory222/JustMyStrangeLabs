package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteCreateDto {
    private String name;

    /**
     * You can provide either existing coordinates id or new coordinates object
     */
    private CoordinatesCreateDto coordinates;

    /**
     * You can provide either existing location id or new location object
     */
    private LocationCreateDto from;

    /**
     * You can provide either existing location id or new location object
     */
    private LocationCreateDto to;

    private long distance;

    private Long rating;
}


