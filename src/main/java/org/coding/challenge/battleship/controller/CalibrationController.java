package org.coding.challenge.battleship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coding.challenge.battleship.dto.RunResponse;
import org.coding.challenge.battleship.dto.SettingsRequest;
import org.coding.challenge.battleship.service.CalibrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/calibration")
@CrossOrigin(origins = "${battleship-calibration.cors.ui}")
@RequiredArgsConstructor
public class CalibrationController {

    private final CalibrationService calibrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PutMapping("/settings")
    public ResponseEntity<Void> settings(@RequestBody @Valid SettingsRequest settingsRequest) {
        calibrationService.settings(settingsRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/run")
    public ResponseEntity<RunResponse> run() {
        return ResponseEntity.ok(calibrationService.run());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MismatchedInputException.class})
    public ResponseEntity<Map<String, String>> handleMismatchedInputException(MismatchedInputException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
