package org.itmo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    @NotNull
    private Coordinates coordinates;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private ZonedDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_location_id", nullable = false)
    @NotNull
    private Location from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_location_id")
    private Location to;

    @Min(2)
    private long distance;

    @Positive
    @Column(name = "rating")
    private Long rating;

    @Column(name = "owner_id", nullable = false)
    @NotNull
    private Long ownerId;

    @PrePersist
    protected void onCreate() {
        this.creationDate = ZonedDateTime.now(ZoneOffset.UTC);
    }
}