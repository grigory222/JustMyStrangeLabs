package web.backend.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.backend.lab4.entity.ResultEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {
    private int x;

    private Double y;

    private int r;

    private boolean result;

    public static PointDTO fromEntity(ResultEntity entity) {
        return PointDTO.builder()
                .x(entity.getX())
                .y(entity.getY())
                .r(entity.getR())
                .result(entity.isResult())
                .build();
    }
}