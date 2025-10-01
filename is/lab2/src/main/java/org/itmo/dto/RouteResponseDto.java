package org.itmo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class RouteResponseDto {
    private Integer id;
    private String name;

    private CoordinatesResponseDto coordinates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime creationDate;
    private LocationResponseDto from;
    private LocationResponseDto to;
    private long distance;
    private Long rating;
}


