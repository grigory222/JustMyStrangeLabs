package web.backend.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDTO {
    boolean result;
    public static ResultDTO of(boolean result) {
        return new ResultDTO(result);
    }
}
