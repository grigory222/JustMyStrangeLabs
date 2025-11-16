package org.itmo.service;

import lombok.RequiredArgsConstructor;
import org.itmo.model.Location;
import org.itmo.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }
}
