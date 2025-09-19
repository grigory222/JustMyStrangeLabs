package org.itmo.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.model.Location;
import org.itmo.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<Location> getAll() {
        return locationService.getAll();
    }
}
