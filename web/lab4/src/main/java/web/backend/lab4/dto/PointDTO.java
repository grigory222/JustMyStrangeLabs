package web.backend.lab4.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {
    //@Min(value = -3)
    //@Max(value = 5)
    private int x;

    //@DecimalMin(value=)
    private Double y;

    //@Min(value = -3)
    //@Max(value = 5)
    private int r;

}