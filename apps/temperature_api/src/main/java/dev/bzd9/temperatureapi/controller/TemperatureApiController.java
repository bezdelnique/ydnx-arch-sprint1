package dev.bzd9.temperatureapi.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@RestController
public class TemperatureApiController {
    @GetMapping("/temperature")
    public TemperatureData getTemperature(@RequestParam String location) {
        double minTemp = 3.0;  // Minimum temperature in Celsius
        double maxTemp = 40.0;   // Maximum temperature in Celsius
        Random random = new Random();

        BigDecimal temperature = BigDecimal.valueOf(minTemp + (maxTemp - minTemp) * random.nextDouble());
        temperature = temperature.setScale(2, RoundingMode.HALF_UP);

        // If no location is provided, use a default based on sensor ID
//        if location == "" {
//            switch sensorID {
//                case "1":
//                    location = "Living Room"
//                case "2":
//                    location = "Bedroom"
//                case "3":
//                    location = "Kitchen"
//                default:
//                    location = "Unknown"
//            }
//        }

        if (location.isBlank()) {
            location = "Unknown";
        }

        // If no sensor ID is provided, generate one based on location
        String sensorId = switch (location) {
            case "Living Room" -> "1";
            case "Bedroom" -> "2";
            case "Kitchen" -> "3";
            default -> "0";
        };

        return new TemperatureData(
                sensorId,
                location,
                temperature
        );
    }


    @Getter
    public static class TemperatureData {
        private String id;
        private String location;
        private BigDecimal value;
        private String unit = "°C";
        private String status = "active";
        private String timestamp;

        public TemperatureData(String id, String location, BigDecimal value) {
            this.id = id;
            this.location = location;
            this.value = value;

            Instant instant = Instant.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            timestamp = formatter.format(instant);
        }
    }

}
