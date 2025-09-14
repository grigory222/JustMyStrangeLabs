package org.itmo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates;

    @Column(updatable = false)
    private ZonedDateTime creationDate;

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
        this.creationDate = ZonedDateTime.now();
    }
}