package org.coding.challenge.battleship.service;

import org.coding.challenge.battleship.dto.RunResponse;
import org.coding.challenge.battleship.dto.SettingsRequest;

public interface CalibrationService {

    void settings(SettingsRequest settingsRequest);

    RunResponse run();
}
