package com.github.rdx7777.gpspositionsaver.generators;

import com.github.rdx7777.gpspositionsaver.model.GpsPositionRequest;

public class GpsPositionRequestGenerator {

    public static GpsPositionRequest getRandomGpsPositionRequest() {
        return GpsPositionRequest.builder()
            .deviceId(WordGenerator.getRandomWord())
            .latitude(WordGenerator.getRandomWord())
            .longitude(WordGenerator.getRandomWord())
            .build();
    }
}
