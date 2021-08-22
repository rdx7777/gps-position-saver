package com.github.rdx7777.gpspositionsaver.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.rdx7777.gpspositionsaver.model.logger.ConsoleColor;
import com.github.rdx7777.gpspositionsaver.model.logger.LogType;
import com.github.rdx7777.gpspositionsaver.model.logger.Logger;
import com.github.rdx7777.gpspositionsaver.repository.LoggerRepository;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    private static LoggerRepository loggerRepository;

    public LoggerService(LoggerRepository loggerRepository) {
        LoggerService.loggerRepository = loggerRepository;
    }

    public static void info(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.INFO, description, callerClassName);
    }

    public static void trace(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.TRACE, description, callerClassName);
    }

    public static void debug(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.DEBUGGED, description, callerClassName);
    }

    public static void warn(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.WARN, description, callerClassName);
    }

    public static void error(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.ERROR, description, callerClassName);
    }

    public static void fatal(String description) {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        generateLog(LogType.FATAL, description, callerClassName);
    }

    private static void generateLog(LogType logType, String description, String callerClassName) {
        Logger logger = new Logger().toBuilder()
            .description(description)
            .timestamp(new Timestamp(System.currentTimeMillis()))
            .logType(logType)
            .sourceClass(callerClassName)
            .build();
        loggerRepository.save(logger);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String stackTrace = LocalDateTime.now().format(formatter)
            + colorize(" [LOGGER] ")
            + colorize(logger.getLogType())
            + " --- "
            + colorizeClass(logger.getSourceClass())
            + "   :   "
            + description;
        System.out.println(stackTrace);
    }

    public static String colorize(LogType logType) {
        switch (logType) {
            case TRACE:
                return ConsoleColor.WHITE + logType.toString() + ConsoleColor.RESET;
            case DEBUGGED:
                return ConsoleColor.CYAN + logType.toString() + ConsoleColor.RESET;
            case INFO:
                return ConsoleColor.GREEN + logType.toString() + ConsoleColor.RESET;
            case WARN:
                return ConsoleColor.YELLOW + logType.toString() + ConsoleColor.RESET;
            case ERROR:
                return ConsoleColor.RED + logType.toString() + ConsoleColor.RESET;
            case FATAL:
                return ConsoleColor.RESET.toString() + ConsoleColor.RED_BACKGROUND.toString() + logType.toString() + ConsoleColor.RESET;
            default:
                return logType.toString();
        }
    }

    public static String colorize(String input) {
        return ConsoleColor.BLUE + input + ConsoleColor.RESET;
    }

    public static String colorizeClass(String input) {
        return ConsoleColor.YELLOW + input + ConsoleColor.RESET;
    }
}
