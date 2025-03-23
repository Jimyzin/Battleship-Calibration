package org.coding.challenge.battleship.service;

import lombok.extern.slf4j.Slf4j;
import org.coding.challenge.battleship.dto.RunResponse;
import org.coding.challenge.battleship.dto.SettingsRequest;
import org.coding.challenge.battleship.entity.CalibrationSetting;
import org.coding.challenge.battleship.enums.TurretLocation;
import org.coding.challenge.battleship.repository.CalibrationSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CalibrationSettingsImpl implements CalibrationService {
    private final CalibrationSettingsRepository calibrationSettingsRepository;
    private final AtomicReference<TurretLocation> turretLocation = new AtomicReference<>(null); // unset at the start
    private final AtomicLong totalDistance = new AtomicLong(0); // unset at the start

    public CalibrationSettingsImpl(CalibrationSettingsRepository calibrationSettingsRepository) {
        this.calibrationSettingsRepository = calibrationSettingsRepository;
    }


    /**
     * This method is used to create a turret setting for testing.
     * It saves the total angular distance (in degree) in a class variable following the formula
     * total angular distance = rotations x (rotation_end_point - rotation_start_point)
     *
     * It also saves the turret identity specified by its location in a class variable.
     * This information is stored in class variables following the problem instruction that
     * the application stores the settings for one turret only.
     *
     * In other words, only the latest stored turret setting can be triggered by a subsequent "run"
     * @param settingsRequest
     */
    @Override
    public void settings(SettingsRequest settingsRequest) {
        log.info("Started saving turret setting: {}", settingsRequest.toString());
        totalDistance.set(settingsRequest.getRotations() *
                (settingsRequest.getRotationEndPoint() - settingsRequest.getRotationStartPoint()));
        turretLocation.set(settingsRequest.getLocation());
        log.info("Successfully saved turret setting");
    }


    /**
     * This method runs the latest turret settings. It triggers run if the total angular distance > 0
     * which signifies a run setting has been created. Else, it does not run.
     *
     * It then gets the test count from H2 database based on the turret identity (or location).
     * Test counts are stored in a non-ephemeral storage, separately for each turret which can withstand application reboots.
     * If the database does not return a row based on a turret identity, then a new record is created for the turret.
     * This only happens for the first time for each turret.
     *
     * The total angular distance and turret location is reset at the end of the "run" for the next turret setting without which
     * it won't trigger a subsequent run again.
     */
    @Override
    public RunResponse run() {

        var numberOfTests = new AtomicInteger(0);
        if (totalDistance.get() == 0) {
            throw new IllegalArgumentException("Calibration Settings do not exist. Please set it before running a test.");
        }
        log.info("Started running turret setting on turret {}", turretLocation.get().toString());

        calibrationSettingsRepository
                .findCalibrationSettingByTurretLocation(turretLocation.get())
                .ifPresentOrElse(calibrationSetting -> {
                    log.info("Found CalibrationSetting in database for turret {}", turretLocation.get().toString());
                    calibrationSettingsRepository.save(calibrationSetting.incrementNumberOfTests());
                    numberOfTests.set(calibrationSetting.getNumberOfTests());
                }, () -> {
                            log.info("Not found CalibrationSetting in database for turret {}. Creating a new row.", turretLocation.get().toString());
                            calibrationSettingsRepository.save(CalibrationSetting.builder()
                                    .numberOfTests(1)
                                    .turretLocation(turretLocation.get())
                                    .build());
                            numberOfTests.set(1);

                        });

        var runResponse =  RunResponse.builder()
                .distanceInDegrees(totalDistance.get())
                .numberOfTests(numberOfTests.get())
                .build();
        log.info("Run Response for turret {} is {}", turretLocation.get().toString(), runResponse.toString());

        resetSettings();
        log.info("Turret settings has been reset");

        return runResponse;
    }

    private void resetSettings() {
        totalDistance.set(0); // reset for next setting
        turretLocation.set(null); // reset for next setting
    }
}
