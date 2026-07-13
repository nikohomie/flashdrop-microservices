package com.flashdrop.catalog.infrastructure.exception;

import com.flashdrop.observability.error.ApiError;
import com.flashdrop.observability.tracing.TraceContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce excepciones a la respuesta estándar { code, service, trace_id, message }.
 * Nunca expone stack traces ni mensajes internos al cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String SERVICE = "catalog-service";

    /** Validaciones de dominio (nombre vacío, precio negativo, etc.). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleDomainValidation(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "CATALOG_VALIDATION_ERROR", ex.getMessage());
    }

    /** Validaciones de Jakarta Bean Validation (@Valid en DTOs). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Datos de entrada inválidos");
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", detail);
    }

    /** Acceso denegado (JWT sin el rol requerido). */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "No tiene permisos para esta operación");
    }

    /** Error genérico: el detalle real va a los logs con trace_id. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error interno del servicio");
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(
                new ApiError(code, SERVICE, TraceContext.currentTraceId(), message));
    }
}
