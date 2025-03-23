package org.coding.challenge.battleship.repository;

import org.coding.challenge.battleship.entity.CalibrationSetting;
import org.coding.challenge.battleship.enums.TurretLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalibrationSettingsRepository extends JpaRepository<CalibrationSetting, String> {

    Optional<CalibrationSetting> findCalibrationSettingByTurretLocation(TurretLocation turretLocation);
}
