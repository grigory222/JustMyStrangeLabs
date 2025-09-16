package org.itmo.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.itmo.model.Route;
import org.itmo.model.Location;
import org.itmo.model.Coordinates;
import org.itmo.repository.RouteRepository;
import org.itmo.repository.LocationRepository;
import org.itmo.repository.CoordinatesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final RouteEventsPublisher eventsPublisher;

    public RouteService(RouteRepository routeRepository,
                        LocationRepository locationRepository,
                        CoordinatesRepository coordinatesRepository,
                        RouteEventsPublisher eventsPublisher) {
        this.routeRepository = routeRepository;
        this.locationRepository = locationRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.eventsPublisher = eventsPublisher;
    }

    public Page<Route> list(String nameEquals, Pageable pageable) {
        if (nameEquals != null && !nameEquals.isEmpty()) {
            return routeRepository.findByName(nameEquals, pageable);
        }
        return routeRepository.findAll(pageable);
    }

    public Route get(@NotNull Integer id) {
        return routeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Route not found: " + id));
    }

    public Route create(@Valid Route route) {
        Long fromId = route.getFrom().getId();
        Long toId = route.getTo().getId();

        if (fromId != null) {
            Location from = locationRepository.findById(fromId)
                    .orElseThrow(() -> new EntityNotFoundException("From location not found: " + fromId));
            route.setFrom(from);
        }
        if (toId != null) {
            Location to = locationRepository.findById(toId)
                    .orElseThrow(() -> new EntityNotFoundException("To location not found: " + toId));
            route.setTo(to);
        }
        Coordinates initialCoords = route.getCoordinates();
        if (initialCoords != null && initialCoords.getId() != null) {
            final Long coordsId = initialCoords.getId();
            Coordinates persistentCoords = coordinatesRepository.findById(coordsId)
                    .orElseThrow(() -> new EntityNotFoundException("Coordinates not found: " + coordsId));
            route.setCoordinates(persistentCoords);
        }
        Route saved = routeRepository.save(route);
        eventsPublisher.publishCreated(saved);
        return saved;
    }

    public Route update(@NotNull Integer id, @Valid Route patch) {
        Route existing = get(id);

        if (patch.getName() != null) existing.setName(patch.getName());
        if (patch.getRating() != null) existing.setRating(patch.getRating());
        if (patch.getDistance() > 0) existing.setDistance(patch.getDistance());

        if (patch.getCoordinates() != null) {
            Coordinates patchCoords = patch.getCoordinates();
            if (patchCoords.getId() != null) {
                final Long coordsId = patchCoords.getId();
                Coordinates persistentCoords = coordinatesRepository.findById(coordsId)
                        .orElseThrow(() -> new EntityNotFoundException("Coordinates not found: " + coordsId));
                existing.setCoordinates(persistentCoords);
            } else {
                existing.setCoordinates(patchCoords);
            }
        }
        Long fromId = patch.getFrom().getId();
        Long toId = patch.getTo().getId();
        if (fromId != null) {
            Location from = locationRepository.findById(fromId)
                    .orElseThrow(() -> new EntityNotFoundException("From location not found: " + fromId));
            existing.setFrom(from);
        } else {
            existing.setFrom(patch.getFrom());
        }
        if (toId != null) {
            Location to = locationRepository.findById(toId)
                    .orElseThrow(() -> new EntityNotFoundException("To location not found: " + toId));
            existing.setTo(to);
        } else {
            existing.setTo(patch.getTo());
        }

        Route saved = routeRepository.save(existing);
        eventsPublisher.publishUpdated(saved);
        return saved;
    }

    public void delete(@NotNull Integer id) {
        Route existing = get(id);
        routeRepository.delete(existing);
        eventsPublisher.publishDeleted(id);
    }

    public long deleteAllByRating(@NotNull Long rating) {
        return routeRepository.deleteByRating(rating);
    }

    public boolean deleteOneByRating(@NotNull Long rating) {
        Optional<Route> routeOpt = routeRepository.findFirstByRating(rating);
        if (routeOpt.isEmpty()) return false;
        Integer id = routeOpt.get().getId();
        routeRepository.delete(routeOpt.get());
        eventsPublisher.publishDeleted(id);
        return true;
    }

    public List<RouteRepository.NameCount> groupByName() {
        return routeRepository.groupByName();
    }

    public Page<Route> findBetween(@NotNull Long fromId, @NotNull Long toId, Pageable pageable) {
        return routeRepository.findByFrom_IdAndTo_Id(fromId, toId, pageable);
    }
}


