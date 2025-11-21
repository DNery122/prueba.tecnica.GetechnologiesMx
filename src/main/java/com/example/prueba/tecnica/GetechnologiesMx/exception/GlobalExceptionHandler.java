package com.example.prueba.tecnica.GetechnologiesMx.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PersonaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonaNotFound(
            PersonaNotFoundException ex) {
        logger.error("Persona no encontrada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "PERSONA_NOT_FOUND",
                ex.getMessage(),
                "");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FacturaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFacturaNotFound(
            FacturaNotFoundException ex) {
        logger.error("Factura no encontrada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "FACTURA_NOT_FOUND",
                ex.getMessage(),
                "");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateIdentifierException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(
            DuplicateIdentifierException ex) {
        logger.error("Identificacion duplicada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "DUPLICATE_IDENTIFIER",
                ex.getMessage(),
                "");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPersonaException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPersona(
            InvalidPersonaException ex) {
        logger.error("Persona invalida: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PERSONA",
                ex.getMessage(),
                "");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Error desconocido: ", ex);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Ha ocurrido un error inesperado",
                "");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}