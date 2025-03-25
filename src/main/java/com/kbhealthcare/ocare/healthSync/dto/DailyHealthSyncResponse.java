package com.kbhealthcare.ocare.healthSync.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DailyHealthSyncResponse {
    String recordkey;
    String date;
    double steps;
    double calories;
    double distance;
}
