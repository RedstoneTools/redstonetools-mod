package com.domain.redstonetools.telemetry.dto;

public record TelemetryException(String stackTrace, boolean isFatal) {
}
