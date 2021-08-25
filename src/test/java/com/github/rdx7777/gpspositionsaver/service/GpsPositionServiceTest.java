package com.github.rdx7777.gpspositionsaver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.rdx7777.gpspositionsaver.generators.GpsPositionGenerator;
import com.github.rdx7777.gpspositionsaver.generators.GpsPositionRequestGenerator;
import com.github.rdx7777.gpspositionsaver.generators.WordGenerator;
import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionMapper;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionRequest;
import com.github.rdx7777.gpspositionsaver.repository.GpsPositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.NonTransientDataAccessException;

@ExtendWith(MockitoExtension.class)
class GpsPositionServiceTest {

    @Mock
    private GpsPositionRepository repository;

    @Mock
    private GpsPositionMapper mapper;

    @InjectMocks
    private GpsPositionService service;

    @Test
    void shouldAddPosition() throws ServiceOperationException {
        // given
        GpsPositionRequest positionRequestToAdd = GpsPositionRequestGenerator.getRandomGpsPositionRequest();
        GpsPosition positionToSave = GpsPositionGenerator.getRandomGpsPosition();
        GpsPosition addedPosition = GpsPositionGenerator.getRandomGpsPosition();
        when(mapper.mapToGpsPosition(positionRequestToAdd)).thenReturn(positionToSave);
        when(repository.save(positionToSave)).thenReturn(addedPosition);

        // when
        GpsPosition result = service.add(positionRequestToAdd);

        // then
        assertEquals(addedPosition, result);
        verify(mapper).mapToGpsPosition(positionRequestToAdd);
        verify(repository).save(positionToSave);
    }

    @Test
    void addMethodShouldThrowServiceOperationExceptionForNullPosition() {
        assertThrows(ServiceOperationException.class, () -> service.add(null));
        verify(mapper).mapToGpsPosition(null);
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("setOfInvalidPositions")
    void addMethodShouldThrowServiceOperationExceptionForInvalidPosition(GpsPositionRequest positionRequest) {
        assertThrows(ServiceOperationException.class, () -> service.add(positionRequest));
        verify(mapper).mapToGpsPosition(positionRequest);
        verify(repository, never()).save(any());
    }

    private static Stream<Arguments> setOfInvalidPositions() {
        return Stream.of(
            Arguments.of(new GpsPositionRequest(WordGenerator.getRandomWord(), WordGenerator.getRandomWord(), null)),
            Arguments.of(new GpsPositionRequest(WordGenerator.getRandomWord(), null, WordGenerator.getRandomWord())),
            Arguments.of(new GpsPositionRequest(null, WordGenerator.getRandomWord(), WordGenerator.getRandomWord()))
        );
    }

    @Test
    void addMethodShouldThrowExceptionWhenAnErrorOccursDuringAddingPositionToDatabase() {
        // given
        GpsPositionRequest positionRequestToAdd = GpsPositionRequestGenerator.getRandomGpsPositionRequest();
        GpsPosition positionToSave = GpsPositionGenerator.getRandomGpsPosition();
        when(mapper.mapToGpsPosition(positionRequestToAdd)).thenReturn(positionToSave);
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).save(positionToSave);

        // then
        assertThrows(ServiceOperationException.class, () -> service.add(positionRequestToAdd));
        verify(mapper).mapToGpsPosition(positionRequestToAdd);
        verify(repository).save(positionToSave);
    }

    @Test
    void shouldReturnPositionByGivenId() throws ServiceOperationException {
        // given
        GpsPosition position = GpsPositionGenerator.getRandomGpsPosition();
        when(repository.findById(1L)).thenReturn(Optional.of(position));

        // when
        Optional<GpsPosition> result = service.getById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(position, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void getByIdMethodShouldThrowServiceOperationExceptionForNullPositionId() {
        assertThrows(ServiceOperationException.class, () -> service.getById(null));
        verify(repository, never()).findById(any());
    }

    @Test
    void getByIdMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingPositionById() {
        // given
        doThrow(new NoSuchElementException()).when(repository).findById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> service.getById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnAllPositionsByDeviceId() throws ServiceOperationException {
        // given
        GpsPosition position = GpsPositionGenerator.getRandomGpsPosition();
        position.setId(1L);

        GpsPosition position1 = GpsPositionGenerator.getRandomGpsPosition();
        position1.setId(2L);
        position1.setDeviceId("12345");

        GpsPosition position2 = GpsPositionGenerator.getRandomGpsPosition();
        position2.setId(3L);

        GpsPosition position3 = GpsPositionGenerator.getRandomGpsPosition();
        position3.setId(4L);
        position3.setDeviceId("12345");

        GpsPosition position4 = GpsPositionGenerator.getRandomGpsPosition();
        position4.setId(5L);
        position4.setDeviceId("12345");

        List<GpsPosition> positions = Arrays.asList(position1, position3, position4);
        when(repository.findAllByDeviceId("12345")).thenReturn(positions);

        // when
        Collection<GpsPosition> result = service.getAllByDeviceId("12345");

        // then
        assertEquals(positions, result);
        verify(repository).findAllByDeviceId("12345");
    }

    @Test
    void getAllByDeviceIdMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllPositionsByDeviceId() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAllByDeviceId("12345");

        // then
        assertThrows(ServiceOperationException.class, () -> service.getAllByDeviceId("12345"));
        verify(repository).findAllByDeviceId("12345");
    }
}
