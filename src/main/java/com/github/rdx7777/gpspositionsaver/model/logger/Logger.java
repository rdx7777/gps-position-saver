package com.github.rdx7777.gpspositionsaver.model.logger;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Logger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    LogType logType;
    String description;
    Timestamp timestamp;
    String sourceClass;
}
