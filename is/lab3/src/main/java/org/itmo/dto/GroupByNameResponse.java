package org.itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupByNameResponse {
    private String name;
    private long total;
    private List<RouteResponseDto> routes;
}