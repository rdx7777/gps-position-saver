package com.github.rdx7777.gpspositionsaver.repository;

import java.util.Optional;

import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsPositionRepository extends JpaRepository<GpsPosition, Long> {

    Optional<GpsPosition> findAllByDeviceId(String deviceId);
}
