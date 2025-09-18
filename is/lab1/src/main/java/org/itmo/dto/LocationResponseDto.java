package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationResponseDto {
    private Long id;
    private Double x;
    private Integer y;
    private Long z;
    private String name;
}


