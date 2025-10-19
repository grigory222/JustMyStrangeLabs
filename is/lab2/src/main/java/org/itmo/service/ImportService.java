package org.itmo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.itmo.dto.CoordinatesDto;
import org.itmo.dto.LocationDto;
import org.itmo.dto.RouteImportDto;
import org.itmo.model.*;
import org.itmo.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    
    private final ImportOperationRepository importOperationRepository;
    private final RouteRepository routeRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public ImportOperation importRoutes(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Create operation record
        ImportOperation operation = new ImportOperation();
        operation.setUsername(username);
        operation.setStatus(ImportOperation.Status.IN_PROGRESS);
        operation = importOperationRepository.save(operation);
        
        try {
            // Parse file
            List<RouteImportDto> routeDtos = parseFile(file);
            
            // Validate and save routes
            int addedCount = 0;
            for (RouteImportDto dto : routeDtos) {
                validateRoute(dto);
                Route route = convertToRoute(dto);
                routeRepository.save(route);
                addedCount++;
            }
            
            // Update operation status
            operation.setStatus(ImportOperation.Status.SUCCESS);
            operation.setAddedCount(addedCount);
            importOperationRepository.save(operation);
            
            return operation;
        } catch (Exception e) {
            // Rollback will happen automatically due to @Transactional
            operation.setStatus(ImportOperation.Status.FAILED);
            operation.setErrorMessage(e.getMessage());
            importOperationRepository.save(operation);
            throw e;
        }
    }
    
    private List<RouteImportDto> parseFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Filename is null");
        }
        
        if (filename.endsWith(".json")) {
            return objectMapper.readValue(file.getInputStream(), new TypeReference<List<RouteImportDto>>() {});
        } else if (filename.endsWith(".csv")) {
            throw new UnsupportedOperationException("CSV parsing not implemented yet");
        } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            throw new UnsupportedOperationException("YAML parsing not implemented yet");
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }
    
    private void validateRoute(RouteImportDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Route name cannot be empty");
        }
        if (dto.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        if (dto.getCoordinates().getY() == null || dto.getCoordinates().getY() <= -488) {
            throw new IllegalArgumentException("Coordinates.y must be > -488");
        }
        if (dto.getFrom() == null) {
            throw new IllegalArgumentException("From location cannot be null");
        }
        if (dto.getTo() == null) {
            throw new IllegalArgumentException("To location cannot be null");
        }
        if (dto.getDistance() == null || dto.getDistance() <= 1) {
            throw new IllegalArgumentException("Distance must be > 1");
        }
        if (dto.getRating() != null && dto.getRating() <= 0) {
            throw new IllegalArgumentException("Rating must be > 0");
        }
    }
    
    private Route convertToRoute(RouteImportDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        
        Route route = new Route();
        route.setName(dto.getName());
        route.setDistance(dto.getDistance());
        route.setRating(dto.getRating());
        route.setCreationDate(ZonedDateTime.now());
        route.setOwnerId(user.getId());
        
        // Обработка coordinates: если есть ID - найти, иначе создать
        Coordinates coordinates;
        if (dto.getCoordinates().getId() != null) {
            coordinates = coordinatesRepository.findById(dto.getCoordinates().getId())
                    .orElseGet(() -> createCoordinates(dto.getCoordinates()));
        } else {
            coordinates = createCoordinates(dto.getCoordinates());
        }
        route.setCoordinates(coordinates);
        
        // Обработка from location: если есть ID - найти, иначе создать
        Location from;
        if (dto.getFrom().getId() != null) {
            from = locationRepository.findById(dto.getFrom().getId())
                    .orElseGet(() -> createLocation(dto.getFrom()));
        } else {
            from = createLocation(dto.getFrom());
        }
        route.setFrom(from);
        
        // Обработка to location: если есть ID - найти, иначе создать
        Location to;
        if (dto.getTo().getId() != null) {
            to = locationRepository.findById(dto.getTo().getId())
                    .orElseGet(() -> createLocation(dto.getTo()));
        } else {
            to = createLocation(dto.getTo());
        }
        route.setTo(to);
        
        return route;
    }
    
    private Coordinates createCoordinates(CoordinatesDto dto) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(dto.getX());
        coordinates.setY(dto.getY());
        return coordinatesRepository.save(coordinates);
    }
    
    private Location createLocation(LocationDto dto) {
        Location location = new Location();
        location.setX(dto.getX());
        location.setY(dto.getY());
        location.setZ(dto.getZ());
        location.setName(dto.getName());
        return locationRepository.save(location);
    }
    
    public List<ImportOperation> getHistory(String username, boolean isAdmin) {
        if (isAdmin) {
            return importOperationRepository.findAllByOrderByCreatedAtDesc();
        } else {
            return importOperationRepository.findByUsernameOrderByCreatedAtDesc(username);
        }
    }
}
