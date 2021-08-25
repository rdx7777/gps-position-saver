package com.github.rdx7777.gpspositionsaver.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.rdx7777.gpspositionsaver.generators.GpsPositionGenerator;
import com.github.rdx7777.gpspositionsaver.generators.GpsPositionRequestGenerator;
import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionMapper;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionRequest;
import com.github.rdx7777.gpspositionsaver.repository.GpsPositionRepository;
import com.github.rdx7777.gpspositionsaver.repository.LoggerRepository;
import com.github.rdx7777.gpspositionsaver.service.GpsPositionService;
import com.github.rdx7777.gpspositionsaver.service.LoggerService;
import com.github.rdx7777.gpspositionsaver.service.ServiceOperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GpsPositionController.class)
class GpsPositionControllerTest {

    @MockBean
    private GpsPositionService service;

    @MockBean
    private LoggerService logger;

    @MockBean
    private LoggerRepository loggerRepository;

    @MockBean
    private GpsPositionMapper positionMapper;

    @MockBean
    private GpsPositionRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldAddPosition() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        GpsPositionRequest positionRequest = GpsPositionRequestGenerator.getRandomGpsPositionRequest();
        GpsPosition position = GpsPositionGenerator.getRandomGpsPositionWithNonNullId();
        when(service.add(positionRequest)).thenReturn(position);

        String url = "/api/positions/add";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(positionRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(position)));

        verify(service).add(positionRequest);
    }

    @Test
    void shouldReturnBadRequestStatusWhenAddingNullPosition() throws Exception {
        String url = "/api/positions/add";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(null))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).add(any());
    }

    @Test
    void shouldReturnInternalServerErrorDuringAddingPositionWhenSomethingWentWrongOnServer() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        GpsPositionRequest positionRequest = GpsPositionRequestGenerator.getRandomGpsPositionRequest();
        when(service.add(positionRequest)).thenThrow(new ServiceOperationException("", new Exception()));

        String url = "/api/positions/add";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(positionRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(service).add(positionRequest);
    }

    @Test
    void shouldReturnPosition() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        GpsPosition position = GpsPositionGenerator.getRandomGpsPositionWithNonNullId();
        Long id = position.getId();
        when(service.getById(id)).thenReturn(Optional.of(position));

        String url = String.format("/api/positions/%d", id);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(position)));

        verify(service).getById(id);
    }

    @Test
    void shouldReturnNotFoundStatusForNonExistingPosition() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        Long id = 1L;
        when(service.getById(id)).thenReturn(Optional.empty());

        String url = String.format("/api/positions/%d", id);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).getById(id);
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingPositionWhenSomethingWentWrongOnServer() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        Long id = 1L;
        when(service.getById(1L)).thenThrow(new ServiceOperationException("", new Exception()));

        String url = String.format("/api/positions/%d", id);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(service).getById(id);
    }

    @Test
    void shouldReturnAllPositionsForSpecificDeviceId() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        String deviceId = "xyz123";
        GpsPosition position1 = GpsPositionGenerator.getRandomGpsPositionWithSpecificDeviceId(deviceId);
        GpsPosition position2 = GpsPositionGenerator.getRandomGpsPositionWithSpecificDeviceId(deviceId);
        GpsPosition position3 = GpsPositionGenerator.getRandomGpsPositionWithSpecificDeviceId(deviceId);
        List<GpsPosition> positions = Arrays.asList(position1, position2, position3);
        when(service.getAllByDeviceId(deviceId)).thenReturn(positions);

        String url = String.format("/api/positions/device/%s", deviceId);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(positions)));

        verify(service).getAllByDeviceId(deviceId);
    }

    @Test
    void shouldReturnNotFoundStatusForNonExistingPositionsForSpecificDeviceId() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        String deviceId = "xyz123";
        List<GpsPosition> positions = new ArrayList<>();
        when(service.getAllByDeviceId(deviceId)).thenReturn(positions);

        String url = String.format("/api/positions/device/%s", deviceId);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).getAllByDeviceId(deviceId);
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllPositionsForSpecificDeviceIdWhenSomethingWentWrongOnServer() throws Exception {
        LoggerService loggerService = new LoggerService(loggerRepository);
        String deviceId = "xyz123";
        when(service.getAllByDeviceId(deviceId)).thenThrow(new ServiceOperationException("", new Exception()));

        String url = String.format("/api/positions/device/%s", deviceId);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(service).getAllByDeviceId(deviceId);
    }
}
