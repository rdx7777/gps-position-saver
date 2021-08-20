package com.github.rdx7777.gpspositionsaver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GpsPositionValidator {

    public static List<String> validate(GpsPosition gpsPosition) {
        if (gpsPosition == null) {
            return Collections.singletonList("GPS position cannot be null.");
        }

        List<String> result = new ArrayList<>();

        result.add(validateDeviceId(gpsPosition.getDeviceId()));
        result.add(validateLatitude(gpsPosition.getLatitude()));
        result.add(validateLongitude(gpsPosition.getLongitude()));

        result = result.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return result;
    }

    private static String validateDeviceId(String deviceId) {
        if (deviceId == null) {
            return "Device id cannot be null.";
        }
        if (deviceId.trim().isEmpty()) {
            return "Device id must contain at least 1 character.";
        }
        return null;
    }

    private static String validateLatitude(String latitude) {
        if (latitude == null) {
            return "Latitude cannot be null.";
        }
        if (latitude.trim().isEmpty()) {
            return "Latitude must contain at least 1 character.";
        }
        return null;
    }

    private static String validateLongitude(String longitude) {
        if (longitude == null) {
            return "Longitude cannot be null.";
        }
        if (longitude.trim().isEmpty()) {
            return "Longitude must contain at least 1 character.";
        }
        return null;
    }
}
