package org.itmo.dto;

import lombok.Data;

@Data
public class AddRouteBetweenDto {
    private String name;
    private Long fromId;
    private Long toId;
    private long distance;
    private Long rating;
    private CoordinatesCreateDto coordinates;
}
