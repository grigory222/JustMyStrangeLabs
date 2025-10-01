package org.itmo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoordinatesCreateDto {
    private Long id;
    private Long x;
    private Float y;
}


