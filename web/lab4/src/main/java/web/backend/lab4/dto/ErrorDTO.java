package web.backend.lab4.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO {
    String error;
    public static ErrorDTO of(String message) {
        return new ErrorDTO(message);
    }
}
