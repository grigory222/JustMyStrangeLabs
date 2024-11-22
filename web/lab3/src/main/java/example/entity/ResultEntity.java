package example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a result entity for storing point data.
 * This entity is mapped to a database table 'lab3_x_test_table' within the schema 'sXXXXXX'.
 * IT SHOULD INCLUDE (in theory) information about the point coordinates (x, y), radius (r) and whether the point is within a certain area (result).
 */
@Entity
@Table(name = "lab3_x_test_table", schema = "s408402")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "s408402.lab3_x_test_table_id_seq", allocationSize = 1)
    private long id;

    private double x;
    private int y;
    private int r;
    private boolean result;
}
