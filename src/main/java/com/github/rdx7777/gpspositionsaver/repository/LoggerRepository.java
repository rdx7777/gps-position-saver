package com.github.rdx7777.gpspositionsaver.repository;

import com.github.rdx7777.gpspositionsaver.model.logger.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerRepository extends JpaRepository<Logger, Long> {
}
