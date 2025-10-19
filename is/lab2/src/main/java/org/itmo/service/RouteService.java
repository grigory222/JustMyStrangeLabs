package org.itmo.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.itmo.dto.*;
import org.itmo.mapper.RouteMapper;
import org.itmo.model.Route;
import org.itmo.model.Location;
import org.itmo.model.Coordinates;
import org.itmo.model.User;
import org.itmo.repository.RouteRepository;
import org.itmo.repository.LocationRepository;
import org.itmo.repository.CoordinatesRepository;
import org.itmo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final UserRepository userRepository;
    private final RouteEventsPublisher eventsPublisher;
    private final RouteMapper routeMapper;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        return user.getId();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void checkOwnership(Route route) {
        if (!isAdmin() && !route.getOwnerId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("You can only access your own routes");
        }
    }

    public Page<RouteResponseDto> list(String nameEquals, Pageable pageable) {
        Long userId = getCurrentUserId();
        boolean admin = isAdmin();
        
        if (nameEquals != null && !nameEquals.isEmpty()) {
            if (admin) {
                return routeRepository.findByName(nameEquals, pageable)
                        .map(routeMapper::toResponseDto);
            } else {
                return routeRepository.findByNameAndOwnerId(nameEquals, userId, pageable)
                        .map(routeMapper::toResponseDto);
            }
        }
        
        if (admin) {
            return routeRepository.findAll(pageable).map(routeMapper::toResponseDto);
        } else {
            return routeRepository.findByOwnerId(userId, pageable).map(routeMapper::toResponseDto);
        }
    }

    public RouteResponseDto get(@NotNull Integer id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + id));
        checkOwnership(route);
        return routeMapper.toResponseDto(route);
    }

    public RouteResponseDto create(@Valid RouteCreateDto dto) {
        Route route = routeMapper.toEntity(dto);
        
        // Set owner
        route.setOwnerId(getCurrentUserId());

        // coordinates: if id provided -> load, else create
        Long coordsId = dto.getCoordinates() != null ? dto.getCoordinates().getId() : null;
        if (coordsId != null) {
            final Long finalCoordsId = coordsId;
            Coordinates persistentCoords = coordinatesRepository.findById(finalCoordsId)
                    .orElseThrow(() -> new EntityNotFoundException("Coordinates not found: " + finalCoordsId));
            route.setCoordinates(persistentCoords);
        } else if (dto.getCoordinates() != null) {
            Coordinates newCoords = routeMapper.toEntity(dto.getCoordinates());
            newCoords = coordinatesRepository.save(newCoords);
            route.setCoordinates(newCoords);
        } else {
            throw new IllegalArgumentException("coordinates are required");
        }

        // from: if id provided -> load, else create
        Long fromId = dto.getFrom() != null ? dto.getFrom().getId() : null;
        
        if (fromId != null) {
            final Long finalFromId = fromId;
            Location from = locationRepository.findById(finalFromId)
                    .orElseThrow(() -> new EntityNotFoundException("From location not found: " + finalFromId));
            route.setFrom(from);
        } else if (dto.getFrom() != null) {
            Location newFrom = routeMapper.toEntity(dto.getFrom());
            newFrom = locationRepository.save(newFrom);
            route.setFrom(newFrom);
        } else {
            throw new IllegalArgumentException("from is required");
        }

        // to: if id provided -> load, else create (nullable overall, but keep logic symmetric)
        Long toId = dto.getTo() != null ? dto.getTo().getId() : null;
        
        if (toId != null) {
            final Long finalToId = toId;
            Location to = locationRepository.findById(finalToId)
                    .orElseThrow(() -> new EntityNotFoundException("To location not found: " + finalToId));
            route.setTo(to);
        } else if (dto.getTo() != null) {
            Location newTo = routeMapper.toEntity(dto.getTo());
            newTo = locationRepository.save(newTo);
            route.setTo(newTo);
        } else {
            route.setTo(null);
        }

        Route saved = routeRepository.saveAndFlush(route);
        eventsPublisher.publishCreated(saved);
        return routeMapper.toResponseDto(saved);
    }

    public RouteResponseDto update(@NotNull Integer id, @Valid RouteCreateDto patch) {
        Route existing = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + id));
        
        checkOwnership(existing);

        if (patch.getName() != null) existing.setName(patch.getName());
        if (patch.getRating() != null) existing.setRating(patch.getRating());
        if (patch.getDistance() > 0) existing.setDistance(patch.getDistance());

        // coordinates update
        if (patch.getCoordinates() != null) {
            Long coordsId = patch.getCoordinates().getId();
            if (coordsId != null) {
                final Long finalCoordsId = coordsId;
                Coordinates persistentCoords = coordinatesRepository.findById(finalCoordsId)
                        .orElseThrow(() -> new EntityNotFoundException("Coordinates not found: " + finalCoordsId));
                existing.setCoordinates(persistentCoords);
            } else if (patch.getCoordinates() != null) {
                Coordinates newCoords = routeMapper.toEntity(patch.getCoordinates());
                newCoords = coordinatesRepository.save(newCoords);
                existing.setCoordinates(newCoords);
            }
        }

        // from update
        if (patch.getFrom() != null) {
            Long fromId = patch.getFrom().getId();
            if (fromId != null) {
                final Long finalFromId = fromId;
                Location from = locationRepository.findById(finalFromId)
                        .orElseThrow(() -> new EntityNotFoundException("From location not found: " + finalFromId));
                existing.setFrom(from);
            } else if (patch.getFrom() != null) {
                Location newFrom = routeMapper.toEntity(patch.getFrom());
                newFrom = locationRepository.save(newFrom);
                existing.setFrom(newFrom);
            }
        }

        // to update
        if (patch.getTo() != null) {
            Long toId = patch.getTo().getId();
            if (toId != null) {
                final Long finalToId = toId;
                Location to = locationRepository.findById(finalToId)
                        .orElseThrow(() -> new EntityNotFoundException("To location not found: " + finalToId));
                existing.setTo(to);
            } else if (patch.getTo() != null) {
                Location newTo = routeMapper.toEntity(patch.getTo());
                newTo = locationRepository.save(newTo);
                existing.setTo(newTo);
            }
        }

        Route saved = routeRepository.saveAndFlush(existing);
        eventsPublisher.publishUpdated(saved);
        return routeMapper.toResponseDto(saved);
    }

    public void delete(@NotNull Integer id) {
        Route existing = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + id));
        checkOwnership(existing);
        routeRepository.delete(existing);
        eventsPublisher.publishDeleted(id);
    }

    public long deleteAllByRating(@NotNull Long rating) {
        Long userId = getCurrentUserId();
        if (isAdmin()) {
            return routeRepository.deleteByRating(rating);
        } else {
            return routeRepository.deleteByRatingAndOwnerId(rating, userId);
        }
    }

    public boolean deleteOneByRating(@NotNull Long rating) {
        Long userId = getCurrentUserId();
        Optional<Route> routeOpt;
        
        if (isAdmin()) {
            routeOpt = routeRepository.findFirstByRating(rating);
        } else {
            routeOpt = routeRepository.findFirstByRatingAndOwnerId(rating, userId);
        }
        
        if (routeOpt.isEmpty()) return false;
        Integer id = routeOpt.get().getId();
        routeRepository.delete(routeOpt.get());
        eventsPublisher.publishDeleted(id);
        return true;
    }

    public List<GroupByNameResponse> groupByName() {
        Long userId = getCurrentUserId();
        List<Route> all;
        
        if (isAdmin()) {
            all = routeRepository.findAll();
        } else {
            all = routeRepository.findByOwnerId(userId, Pageable.unpaged()).getContent();
        }
        
        Map<String, List<Route>> grouped = all.stream().collect(Collectors.groupingBy(Route::getName));
        List<GroupByNameResponse> result = new ArrayList<>();
        for (Map.Entry<String, List<Route>> entry : grouped.entrySet()) {
            String name = entry.getKey();
            List<RouteResponseDto> routes = entry.getValue().stream().map(routeMapper::toResponseDto).toList();
            result.add(new GroupByNameResponse(name, routes.size(), routes));
        }
        return result;
    }

    public Page<RouteResponseDto> findBetween(@NotNull Long fromId, @NotNull Long toId, Pageable pageable) {
        Long userId = getCurrentUserId();
        if (isAdmin()) {
            return routeRepository.findRoutesBetweenLocations(fromId, toId, pageable).map(routeMapper::toResponseDto);
        } else {
            return routeRepository.findRoutesBetweenLocationsAndOwnerId(fromId, toId, userId, pageable).map(routeMapper::toResponseDto);
        }
    }

    public RouteResponseDto addRouteBetween(AddRouteBetweenDto dto) {
        Location from = locationRepository.findById(dto.getFromId())
                .orElseThrow(() -> new EntityNotFoundException("From location not found: " + dto.getFromId()));
        Location to = locationRepository.findById(dto.getToId())
                .orElseThrow(() -> new EntityNotFoundException("To location not found: " + dto.getToId()));

        Coordinates coordinates = routeMapper.toEntity(dto.getCoordinates());
        coordinates = coordinatesRepository.save(coordinates);

        Route route = new Route();
        route.setName(dto.getName());
        route.setFrom(from);
        route.setTo(to);
        route.setDistance(dto.getDistance());
        route.setRating(dto.getRating());
        route.setCoordinates(coordinates);
        route.setOwnerId(getCurrentUserId());

        route = routeRepository.save(route);
        eventsPublisher.publishCreated(route);
        return routeMapper.toResponseDto(route);
    }
}


