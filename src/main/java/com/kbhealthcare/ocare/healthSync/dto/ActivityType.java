package com.kbhealthcare.ocare.healthSync.dto;

public enum ActivityType {
    STEPS,
    ;

    public static ActivityType find(String value) {
        if (value.toLowerCase().equals("steps")) {
            return STEPS;
        }

        throw new IllegalArgumentException("Unknown activity type: " + value);
    }
}
