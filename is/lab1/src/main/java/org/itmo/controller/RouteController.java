package org.itmo.controller;

import jakarta.validation.Valid;
import org.itmo.model.Route;
import org.itmo.service.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public Page<Route> list(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false) String order,
                            @RequestParam(required = false) String nameEquals) {
        Sort sortSpec = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            Sort.Direction dir = (order != null && order.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortSpec = Sort.by(dir, sort);
        }
        Pageable pageable = PageRequest.of(page, size, sortSpec);
        return routeService.list(nameEquals, pageable);
    }

    @GetMapping("/{id}")
    public Route get(@PathVariable Integer id) {
        return routeService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Route create(@Valid @RequestBody Route route) {
        return routeService.create(route);
    }

    @PatchMapping("/{id}")
    public Route update(@PathVariable Integer id,
                        @RequestBody Route patch) {
        return routeService.update(id, patch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Спец-операции
    @DeleteMapping("/by-rating")
    public Map<String, Object> deleteAllByRating(@RequestParam Long rating) {
        long count = routeService.deleteAllByRating(rating);
        return Map.of("deleted", count);
    }

    @DeleteMapping("/one-by-rating")
    public ResponseEntity<?> deleteOneByRating(@RequestParam Long rating) {
        boolean deleted = routeService.deleteOneByRating(rating);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No route with given rating"));
    }

    @GetMapping("/group-by-name")
    public Object groupByName() {
        return routeService.groupByName();
    }

    @GetMapping("/between")
    public Page<Route> findBetween(@RequestParam Long fromId,
                                   @RequestParam Long toId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false) String order) {
        Sort sortSpec = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            Sort.Direction dir = (order != null && order.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortSpec = Sort.by(dir, sort);
        }
        Pageable pageable = PageRequest.of(page, size, sortSpec);
        return routeService.findBetween(fromId, toId, pageable);
    }
}


