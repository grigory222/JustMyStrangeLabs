package org.itmo.repository;

import org.itmo.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    Page<Route> findByName(String name, Pageable pageable);

    Page<Route> findByFrom_IdAndTo_Id(Long fromId, Long toId, Pageable pageable);

    @Query("SELECT r FROM Route r WHERE (r.from.id = :fromId AND r.to.id = :toId) OR (r.from.id = :toId AND r.to.id = :fromId)")
    Page<Route> findRoutesBetweenLocations(@Param("fromId") Long fromId, @Param("toId") Long toId, Pageable pageable);

    long deleteByRating(Long rating);

    Optional<Route> findFirstByRating(Long rating);

    @Query("select r.name as name, count(r) as total from Route r group by r.name")
    List<NameCount> groupByName();

    interface NameCount {
        String getName();
        long getTotal();
    }
}