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
}
