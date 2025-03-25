package com.kbhealthcare.ocare.healthSync.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthSyncResponse {
    String recordkey;
    String date;
    double steps;
    double calories;
    double distance;
}
