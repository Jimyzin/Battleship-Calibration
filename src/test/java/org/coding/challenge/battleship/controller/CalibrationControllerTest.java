package org.coding.challenge.battleship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coding.challenge.battleship.service.CalibrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalibrationController.class)
public class CalibrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalibrationService calibrationService;

    /**
     * GIVEN valid SettingsRequest
     * WHEN call /calibration/run
     * THEN run successfully
     */
    @Test
    public void test_runSettings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/calibration/run"))
                .andExpect(status().isOk());
    }

    /**
     * GIVEN an invalid SettingsRequest with caliber < 102
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenCaliberLessThan102() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 101,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 100,\n" +
                                "    \"rotation_end_point\": 120,\n" +
                                "    \"rotations\": 3\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("caliber")).isEqualTo("Minimum value of caliber is 102");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with caliber > 450
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenCaliberGreaterThen450() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 550,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 100,\n" +
                                "    \"rotation_end_point\": 120,\n" +
                                "    \"rotations\": 3\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("caliber")).isEqualTo("Maximum value of caliber is 450");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest rotations <= 0,
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationsLessThan1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 20,\n" +
                                "    \"rotation_end_point\": 120,\n" +
                                "    \"rotations\": -1\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("rotations")).isEqualTo("Minimum value of rotations is 1");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with rotation_start_point < 0",
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationStartPointLessThan0() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": -1,\n" +
                                "    \"rotation_end_point\": 120,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("rotationStartPoint")).isEqualTo("Minimum value of rotation_start_point is 0");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with rotation_start_point > 180",
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationStartPointGreaterThen180() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 181,\n" +
                                "    \"rotation_end_point\": 120,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("rotationStartPoint")).isEqualTo("Maximum value of rotation_start_point is 180");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with rotation_end_point < -1",
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationEndPointLessThan0() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 10,\n" +
                                "    \"rotation_end_point\": -1,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("rotationEndPoint")).isEqualTo("Minimum value of rotation_end_point is 0");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with rotation_end_point > 180",
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationEndPointGreaterThen180() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 10,\n" +
                                "    \"rotation_end_point\": 181,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("rotationEndPoint")).isEqualTo("Maximum value of rotation_end_point is 180");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with rotation_end_point < rotation_start_point",
     * WHEN call /calibration/settings
     * THEN throw ValidationException
     */
    @Test
    public void testValidationException_whenRotationEndPointLesserThanRotationStartPoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Stern\",\n" +
                                "    \"rotation_start_point\": 100,\n" +
                                "    \"rotation_end_point\": 99,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("settingsRequest")).isEqualTo("rotation_end_point must be greater than rotation_start_point");
                });
    }

    /**
     * GIVEN an invalid SettingsRequest with turret = invalid value",
     * WHEN call /calibration/settings
     * THEN throw MismatchedInputException
     */
    @Test
    public void testValidationException_whenRotationEndPointLesserThanRotationStartPoint2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/calibration/settings")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caliber\": 150,\n" +
                                "    \"location\": \"Front\",\n" +
                                "    \"rotation_start_point\": 100,\n" +
                                "    \"rotation_end_point\": 99,\n" +
                                "    \"rotations\": 9\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Map<String, String> errors = parseJson(result.getResponse().getContentAsString());
                    assertThat((String) errors.get("error")).contains("Cannot deserialize value of type `org.coding.challenge.battleship.enums.TurretLocation`");
                });
    }

    private Map<String, String> parseJson(String json) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        Map<String, String> errors = objectMapper.readValue(json, Map.class);
        return errors;
    }
}
