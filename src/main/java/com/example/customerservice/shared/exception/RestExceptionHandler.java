package com.example.customerservice.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.customerservice.config.RequestIdFilter.REQUEST_ID_HEADER;
import static com.example.customerservice.config.RequestIdFilter.MDC_REQUEST_ID_KEY;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Pattern QUOTED_VALUE = Pattern.compile("'([^']*)'");

    private String getRequestId(HttpServletRequest request) {
        String fromMdc = MDC.get(MDC_REQUEST_ID_KEY);
        if (fromMdc != null && !fromMdc.isBlank()) return fromMdc;
        String fromHeader = request.getHeader(REQUEST_ID_HEADER);
        return fromHeader != null ? fromHeader : "-";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        String path = request.getDescription(false);
        String requestId = "-";
        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", message, path, requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(REQUEST_ID_HEADER, requestId).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        String paramName = ex.getName();
        Object value = ex.getValue();
        Class<?> reqType = ex.getRequiredType();
        String expected = reqType != null ? reqType.getSimpleName() : "valor válido";
        String message;
        if (reqType != null && (reqType.equals(java.util.UUID.class) || "UUID".equalsIgnoreCase(reqType.getSimpleName()) || "Guid".equalsIgnoreCase(reqType.getSimpleName()))) {
            message = String.format("El parámetro '%s' debe ser un UUID/GUID válido. Formato ejemplo: '3fa85f64-5717-4562-b3fc-2c963f66afa6'", paramName);
        } else {
            message = String.format("Parámetro '%s' inválido: el valor '%s' no es un %s válido", paramName, value, expected);
        }
        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", message, request.getRequestURI(), requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(REQUEST_ID_HEADER, requestId).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getRequestURI(), requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header(REQUEST_ID_HEADER, requestId).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(), request.getRequestURI(), requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(REQUEST_ID_HEADER, requestId).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        // Extraer primer valor entrecomillado del mensaje SQL si existe
        Optional<String> maybeValue = Optional.empty();
        if (msg != null) {
            Matcher m = QUOTED_VALUE.matcher(msg);
            if (m.find()) {
                maybeValue = Optional.ofNullable(m.group(1));
            }
        }

        String friendly;

        // Si se extrajo un valor, detectar si es email o número
        if (maybeValue.isPresent()) {
            String val = maybeValue.get();
            if (val.contains("@")) {
                friendly = String.format("El correo '%s' ya está registrado", val);
            } else if (val.matches("\\d+")) {
                friendly = String.format("El número de documento '%s' ya está registrado", val);
            } else {
                // No es claramente email ni solo números: intentar detectar por campo en el mensaje
                String lower = msg.toLowerCase();
                if (lower.contains("email")) {
                    friendly = String.format("El correo '%s' ya está registrado", val);
                } else if (lower.contains("document_number") || lower.contains("document number") || lower.contains("document")) {
                    friendly = String.format("El número de documento '%s' ya está registrado", val);
                } else {
                    friendly = "Violación de integridad de datos: valor duplicado";
                }
            }
        } else {
            // No se extrajo valor; detectar por nombre de campo en mensaje
            String lower = msg != null ? msg.toLowerCase() : "";
            if (lower.contains("email") || lower.contains("@")) {
                friendly = "El correo ya está registrado";
            } else if (lower.contains("document_number") || lower.contains("document number") || lower.contains("document")) {
                friendly = "El número de documento ya está registrado";
            } else if (lower.contains("duplicate key") || lower.contains("unique index") || lower.contains("uk_")) {
                friendly = "Registro duplicado (clave única)";
            } else {
                friendly = "Violación de integridad de datos" + (msg != null ? (": " + msg) : "");
            }
        }

        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", friendly, request.getRequestURI(), requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(REQUEST_ID_HEADER, requestId).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage(), request.getRequestURI(), requestId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(REQUEST_ID_HEADER, requestId).body(error);
    }
}
