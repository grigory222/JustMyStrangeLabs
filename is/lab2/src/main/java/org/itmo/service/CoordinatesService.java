package org.itmo.service;

import lombok.RequiredArgsConstructor;
import org.itmo.model.Coordinates;
import org.itmo.repository.CoordinatesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;

    public List<Coordinates> getAll() {
        return coordinatesRepository.findAll();
    }
}
