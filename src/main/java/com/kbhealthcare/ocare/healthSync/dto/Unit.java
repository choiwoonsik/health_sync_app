package com.kbhealthcare.ocare.healthSync.dto;

public enum Unit {
    KM,
    KCAL,
    ;

    public static Unit findOrNull(String unit) {
        return switch (unit.toLowerCase()) {
            case "km" -> KM;
            case "kcal" -> KCAL;
            default -> null;
        };
    }
}
