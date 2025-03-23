package org.coding.challenge.battleship.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.coding.challenge.battleship.enums.TurretLocation;
import org.coding.challenge.battleship.validation.RotationEndPointValidation;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@RotationEndPointValidation
public class SettingsRequest {

    @Min(value = 102, message = "Minimum value of caliber is 102")
    @Max(value = 450, message = "Maximum value of caliber is 450")
    private long caliber;
    private TurretLocation location;

    @Min(value = 0, message = "Minimum value of rotation_start_point is 0")
    @Max(value = 180, message = "Maximum value of rotation_start_point is 180")
    private long rotationStartPoint;

    @Min(value = 0, message = "Minimum value of rotation_end_point is 0")
    @Max(value = 180, message = "Maximum value of rotation_end_point is 180")
    private long rotationEndPoint;

    @Min(value = 1, message = "Minimum value of rotations is 1")
    private int rotations;

}
