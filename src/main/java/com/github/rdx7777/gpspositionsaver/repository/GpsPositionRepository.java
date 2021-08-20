package com.github.rdx7777.gpspositionsaver.repository;

import java.util.Collection;

import com.github.rdx7777.gpspositionsaver.model.GpsPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsPositionRepository extends JpaRepository<GpsPosition, Long> {

    Collection<GpsPosition> findAllByDeviceId(String deviceId);
}
