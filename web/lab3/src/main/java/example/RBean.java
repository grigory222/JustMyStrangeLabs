package example;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.validator.ValidatorException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Managed bean for handling Y coordinate value in JSF application.
 * Offers methods to get and set Y value, and a validator to ensure the Y value falls within a specified range.
 */
@Data
@NoArgsConstructor
public class RBean implements Serializable {
    private Integer r = 0;

    public void validateRBeanValue(Object o){
        if (o == null){
            FacesMessage message = new FacesMessage("Y value should be in {1, 2, 3, 4, 5}");
            throw new ValidatorException(message);
        }
    }
}
