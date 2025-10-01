package org.itmo.mapper;

import org.itmo.dto.*;
import org.itmo.model.Coordinates;
import org.itmo.model.Location;
import org.itmo.model.Route;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteMapper {

    // Entity -> Response DTO
    RouteResponseDto toResponseDto(Route route);
    CoordinatesResponseDto toResponseDto(Coordinates coordinates);
    LocationResponseDto toResponseDto(Location location);

    // Create DTO -> Entity (без связей, связи проставляем в сервисе)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coordinates", ignore = true)
    @Mapping(target = "from", ignore = true)
    @Mapping(target = "to", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    Route toEntity(RouteCreateDto dto);

    Coordinates toEntity(CoordinatesCreateDto dto);
    Location toEntity(LocationCreateDto dto);
}



