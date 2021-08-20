package com.github.rdx7777.gpspositionsaver.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.github.rdx7777.gpspositionsaver.generators.GpsPositionGenerator;
import com.github.rdx7777.gpspositionsaver.generators.WordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GpsPositionValidatorTest {

    private GpsPosition correctPosition;

    @BeforeEach
    void setup() {
        correctPosition = GpsPositionGenerator.getRandomGpsPosition();
    }

    @Test
    void shouldValidateCorrectPosition() {
        // when
        List<String> resultOfValidation = GpsPositionValidator.validate(correctPosition);

        // then
        assertEquals(Collections.emptyList(), resultOfValidation);
    }

    @Test
    void shouldValidateNullPosition() {
        // when
        List<String> resultOfValidation = GpsPositionValidator.validate(null);

        // then
        assertEquals(Collections.singletonList("GPS position cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("setOfDeviceIdsAndValidationResults")
    void shouldValidateDeviceId(String deviceId, List<String> expected) {
        // given
        GpsPosition position = correctPosition.toBuilder().deviceId(deviceId).build();

        // when
        List<String> resultOfValidation = GpsPositionValidator.validate(position);

        // then
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfDeviceIdsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Device id cannot be null.")),
            Arguments.of("", Collections.singletonList("Device id must contain at least 1 character.")),
            Arguments.of(WordGenerator.getRandomWord(), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("setOfLatitudesAndValidationResults")
    void shouldValidateLatitude(String latitude, List<String> expected) {
        // given
        GpsPosition position = correctPosition.toBuilder().latitude(latitude).build();

        // when
        List<String> resultOfValidation = GpsPositionValidator.validate(position);

        // then
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfLatitudesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Latitude cannot be null.")),
            Arguments.of("", Collections.singletonList("Latitude must contain at least 1 character.")),
            Arguments.of(WordGenerator.getRandomWord(), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("setOfLongitudesAndValidationResults")
    void shouldValidateLongitude(String longitude, List<String> expected) {
        // given
        GpsPosition position = correctPosition.toBuilder().longitude(longitude).build();

        // when
        List<String> resultOfValidation = GpsPositionValidator.validate(position);

        // then
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfLongitudesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Longitude cannot be null.")),
            Arguments.of("", Collections.singletonList("Longitude must contain at least 1 character.")),
            Arguments.of(WordGenerator.getRandomWord(), Collections.emptyList())
        );
    }
}
