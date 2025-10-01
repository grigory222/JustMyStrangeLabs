package org.itmo.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.model.Coordinates;
import org.itmo.service.CoordinatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @GetMapping
    public List<Coordinates> getAll() {
        return coordinatesService.getAll();
    }
}
