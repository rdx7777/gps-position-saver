package com.github.rdx7777.gpspositionsaver.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Collection;
import java.util.Optional;

import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import com.github.rdx7777.gpspositionsaver.model.GpsPositionRequest;
import com.github.rdx7777.gpspositionsaver.service.GpsPositionService;
import com.github.rdx7777.gpspositionsaver.service.LoggerService;
import com.github.rdx7777.gpspositionsaver.service.ServiceOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/positions")
public class GpsPositionController {

    private final GpsPositionService service;

    public GpsPositionController(GpsPositionService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @ApiResponse(responseCode = "201",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(oneOf = GpsPosition.class)))
    @ApiResponse(responseCode = "400",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Empty position", value = "Attempt to add null position.")
            }))
    @ApiResponse(responseCode = "500",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Server error", value = "Message of error.")
            }))
    public ResponseEntity<?> add(@RequestBody (required = false) GpsPositionRequest positionRequest) {
        try {
            if (positionRequest == null) {
                return new ResponseEntity<>("Attempt to add null position.", HttpStatus.BAD_REQUEST);
            }
            GpsPosition position = service.add(positionRequest);
            LoggerService.info(String.format("GPS position added successfully with id: %d. Device id: %s, latitude: %s, longitude: %s.",
                position.getId(), position.getDeviceId(), position.getLatitude(), position.getLongitude()));
            return new ResponseEntity(position, HttpStatus.CREATED);
        } catch (ServiceOperationException e) {
            LoggerService.error("An error occurred when adding GPS position: " + e.getMessage() + " : " + e.getStatus());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(oneOf = GpsPosition.class)))
    @ApiResponse(responseCode = "404",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Position does not exist", value = "GPS position with id %d does not exist.")
            }))
    @ApiResponse(responseCode = "500",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Server error", value = "Message of error.")
            }))
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Optional<GpsPosition> position = service.getById(id);
            if (position.isPresent()) {
                LoggerService.info(String.format("Request for getting GPS position with id: %d.", id));
                return ResponseEntity.ok(position);
            } else {
                LoggerService.info(String.format("Request for getting non existing GPS position with id: %d.", id));
                return new ResponseEntity<>(String.format("GPS position with id %d does not exist.", id), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceOperationException e) {
            LoggerService.error(String.format("An error occurred when getting GPS position with id: %d. Details: ", id) + e.getMessage() + " : " + e.getStatus());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    @GetMapping("/device/{id}")
    @ApiResponse(responseCode = "200",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = GpsPosition.class))))
    @ApiResponse(responseCode = "404",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Empty database", value = "No GPS positions in database for device id %s.")
            }))
    @ApiResponse(responseCode = "500",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(oneOf = String.class),
            examples = {
                @ExampleObject(name = "Server error", value = "Message of error.")
            }))
    public ResponseEntity<?> getAllByDeviceId(@PathVariable("id") String id) {
        try {
            Collection<GpsPosition> positions = service.getAllByDeviceId(id);
            if (!positions.isEmpty()) {
                LoggerService.info(String.format("Request for getting all GPS positions for device id: %s.", id));
                return ResponseEntity.ok(positions);
            } else {
                LoggerService.info(String.format("Request for getting non existing GPS positions for device id: %s.", id));
                return new ResponseEntity<>(String.format("No GPS positions in database for device id %s.", id), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceOperationException e) {
            LoggerService.error(String.format("An error occurred when getting all GPS positions for device id: %s. Details: ", id) + e.getMessage() + " : " + e.getStatus());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }
}
