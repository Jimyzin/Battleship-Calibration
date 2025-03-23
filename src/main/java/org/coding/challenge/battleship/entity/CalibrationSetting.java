package org.coding.challenge.battleship.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coding.challenge.battleship.enums.TurretLocation;

@Entity
@Table(name = "CALIBRATION_SETTING")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationSetting {

    @Id
    private TurretLocation turretLocation;
    private int numberOfTests;

    public CalibrationSetting incrementNumberOfTests() {
        numberOfTests++;
        return this;
    }

}
