package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoordinatesResponseDto {
    private Long id;
    private Long x;
    private Float y;
}


