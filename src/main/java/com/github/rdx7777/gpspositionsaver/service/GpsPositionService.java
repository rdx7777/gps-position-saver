package com.github.rdx7777.gpspositionsaver.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionMapper;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionRequest;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionValidator;
import com.github.rdx7777.gpspositionsaver.repository.GpsPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class GpsPositionService {

    private final GpsPositionMapper mapper;
    private final GpsPositionRepository repository;

    public GpsPositionService(GpsPositionMapper mapper, GpsPositionRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public GpsPosition add(GpsPositionRequest positionRequest) throws ServiceOperationException {
        GpsPosition position = mapper.mapToGpsPosition(positionRequest);
        List<String> validations = GpsPositionValidator.validate(position);
        if (!validations.isEmpty()) {
            throw new ServiceOperationException(String.format(
                "Attempt to add GPS position with following invalid fields: %n%s", validations));
        }
        try {
            return repository.save(position);
        } catch (Exception e) {
            throw new ServiceOperationException("An error occurred during adding GPS position.", e);
        }
    }

    public Optional<GpsPosition> getById(Long id) throws ServiceOperationException {
        if (id == null) {
            throw new ServiceOperationException("Id cannot be null.");
        }
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new ServiceOperationException(String.format("An error occurred during getting GPS position by id: %d.", id), e);
        }
    }

    public Collection<GpsPosition> getAllByDeviceId(String deviceId) throws ServiceOperationException {
        if (deviceId == null) {
            throw new ServiceOperationException("Device id cannot be null.");
        }
        try {
            return repository.findAllByDeviceId(deviceId);
        } catch (Exception e) {
            throw new ServiceOperationException(String.format("An error occurred during getting all GPS positions by device id: %s.", deviceId), e);
        }
    }
}
