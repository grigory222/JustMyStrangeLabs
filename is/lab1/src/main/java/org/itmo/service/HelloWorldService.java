package org.itmo.service;

import org.itmo.model.Location;
import org.itmo.model.Coordinates;
import org.itmo.model.Route;
import org.itmo.repository.LocationRepository;
import org.itmo.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HelloWorldService {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public HelloWorldService(RouteRepository routeRepository, LocationRepository locationRepository) {
        this.routeRepository = routeRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void createTestData() {
        // Создаем тестовые данные, только если в БД еще нет маршрутов
        if (routeRepository.count() == 0) {
            System.out.println("База данных пуста. Создаю тестовые данные...");

            Location fromLocation = new Location();
            fromLocation.setName("Старт");
            fromLocation.setX(10.0);
            fromLocation.setY(20);
            fromLocation.setZ(30L);
            locationRepository.save(fromLocation);

            Location toLocation = new Location();
            toLocation.setName("Финиш");
            toLocation.setX(40.0);
            toLocation.setY(50);
            toLocation.setZ(60L);
            locationRepository.save(toLocation);

            // Создаем маршрут
            Route testRoute = new Route();
            testRoute.setName("Тестовый маршрут 1");
            testRoute.setDistance(123L);
            testRoute.setRating(5L);

            Coordinates coords = new Coordinates();
            coords.setX(11L);
            coords.setY(22.0f);
            testRoute.setCoordinates(coords); // Координаты сохранятся каскадно с Route

            testRoute.setFrom(fromLocation);
            testRoute.setTo(toLocation);

            routeRepository.save(testRoute);
            System.out.println("Тестовые данные созданы.");
        }
    }

    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
}