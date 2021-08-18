package com.github.rdx7777.gpspositionsaver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GpsPositionRequest {

    private String deviceId;
    private String latitude;
    private String longitude;
}
