package org.coding.challenge.battleship.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.coding.challenge.battleship.dto.SettingsRequest;

/**
 * This is a custom ConstraintValidator to validate that rotation_end_point is always greater than
 * rotation_start_point.
 * If this validation fails, no further processing is done to the request body.
 */
public class RotationEndPointValidator implements ConstraintValidator<RotationEndPointValidation, SettingsRequest> {

    @Override
    public boolean isValid(SettingsRequest settingsRequest, ConstraintValidatorContext constraintValidatorContext) {
        return settingsRequest.getRotationEndPoint() > settingsRequest.getRotationStartPoint();
    }
}
