package com.github.rdx7777.gpspositionsaver.generators;

import com.github.rdx7777.gpspositionsaver.model.GpsPosition;

public class GpsPositionGenerator {

    public static GpsPosition getRandomGpsPosition() {
        return GpsPosition.builder()
            .id(null)
            .deviceId(WordGenerator.getRandomWord())
            .latitude(WordGenerator.getRandomWord())
            .longitude(WordGenerator.getRandomWord())
            .build();
    }

    public static GpsPosition getRandomGpsPositionWithNonNullId() {
        return GpsPosition.builder()
            .id(IdGenerator.getNextId())
            .deviceId(WordGenerator.getRandomWord())
            .latitude(WordGenerator.getRandomWord())
            .longitude(WordGenerator.getRandomWord())
            .build();
    }

    public static GpsPosition getRandomGpsPositionWithSpecificDeviceId(String deviceId) {
        return GpsPosition.builder()
            .id(IdGenerator.getNextId())
            .deviceId(deviceId)
            .latitude(WordGenerator.getRandomWord())
            .longitude(WordGenerator.getRandomWord())
            .build();
    }
}
