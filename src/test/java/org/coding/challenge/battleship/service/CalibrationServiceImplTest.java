package org.coding.challenge.battleship.service;

import org.coding.challenge.battleship.dto.RunResponse;
import org.coding.challenge.battleship.dto.SettingsRequest;
import org.coding.challenge.battleship.entity.CalibrationSetting;
import org.coding.challenge.battleship.enums.TurretLocation;
import org.coding.challenge.battleship.repository.CalibrationSettingsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        CalibrationSettingsImpl.class,
        CalibrationSettingsRepository.class
})
public class CalibrationServiceImplTest {

    @Autowired
    private CalibrationService calibrationService;

    @MockitoBean
    private CalibrationSettingsRepository calibrationSettingsRepository;

    /**
     * GIVEN a valid SettingsRequest
     * WHEN create settings using it
     * THEN successfully create settings
     */
    @Test
    public void givenValidSettingsRequest_whenSettings_thenSuccessful() {
        var settingsRequest = createSettingsRequest(TurretLocation.Bow, 20, 120, 3);
        try {
            calibrationService.settings(settingsRequest);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * GIVEN a valid SettingsRequest has been set for a specific TurretLocation
     * WHEN create run test on the turret
     * THEN run successfully and return test results
     */
    @Test
    public void givenValidSettings_whenRun_thenSuccessful() {
        var numberOfTests = 2;

        // mock
        when(calibrationSettingsRepository.findCalibrationSettingByTurretLocation(any(TurretLocation.class)))
                .thenReturn(Optional.of(CalibrationSetting.builder()
                                .numberOfTests(numberOfTests)
                                .turretLocation(TurretLocation.Stern)
                                .build()));

        // creating a test setting
        var settingsRequest = createSettingsRequest(TurretLocation.Stern, 20, 120, 3);
        calibrationService.settings(settingsRequest);
        RunResponse runResponse = null;

        try {
            runResponse = calibrationService.run();
        } catch (Exception e) {
            fail();
        }

        assertThat(runResponse.getNumberOfTests()).isEqualTo(numberOfTests + 1);
        assertThat(runResponse.getDistanceInDegrees()).isEqualTo(300);
    }

    /**
     * GIVEN no SettingsRequest has been set for a specific TurretLocation
     * WHEN create run test on the turret
     * THEN throw an IllegalArgumentException
     */
    @Test
    public void givenNoSettings_whenRun_thenThrowIllegalArgumentException() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> calibrationService.run());
        var expectedMessage = "Calibration Settings do not exist. Please set it before running a test.";
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }



    private SettingsRequest createSettingsRequest(TurretLocation turretLocation, int rotationStartPoint, int rotationEndPoint, int rotations) {
        var settingsRequest = new SettingsRequest();
        settingsRequest.setCaliber(110);
        settingsRequest.setLocation(turretLocation);
        settingsRequest.setRotationStartPoint(rotationStartPoint);
        settingsRequest.setRotationEndPoint(rotationEndPoint);
        settingsRequest.setRotations(rotations);
        return settingsRequest;
    }

}
