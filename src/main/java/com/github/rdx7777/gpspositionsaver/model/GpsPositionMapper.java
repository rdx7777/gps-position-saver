package com.github.rdx7777.gpspositionsaver.model;

import org.springframework.stereotype.Component;

@Component
public class GpsPositionMapper {

    public GpsPosition mapToGpsPosition(GpsPositionRequest positionRequest) {
        if (positionRequest == null) {
            return null;
        }
        return GpsPosition.builder()
            .id(null)
            .deviceId(positionRequest.getDeviceId())
            .latitude(positionRequest.getLatitude())
            .longitude(positionRequest.getLongitude())
            .build();
    }
}
