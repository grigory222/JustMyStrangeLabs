package org.itmo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_location_id")
    private Location from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_location_id")
    private Location to;

    private long distance;
    private Long rating;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}