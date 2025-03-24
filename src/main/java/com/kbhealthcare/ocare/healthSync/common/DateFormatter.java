package com.kbhealthcare.ocare.healthSync.common;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public final static DateTimeFormatter yyyyMMdd_HHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public final static DateTimeFormatter yyyyMMdd_HHmmss_Z = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
    public final static DateTimeFormatter yyyyMMddTHHmmssZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
}
