package com.kbhealthcare.ocare.healthSync.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamChunkDelegator {
    public static <T> List<List<T>> chunkStream(Stream<T> stream, int chunkSize) {
        if (chunkSize < 1) {
            throw new IllegalArgumentException("CHUNK_SIZE 크기는 최소 1 이상이어야 합니다.");
        }

        AtomicInteger counter = new AtomicInteger();
        Map<Integer, List<T>> grouped = stream.collect(Collectors.groupingBy(e -> counter.getAndIncrement() / chunkSize));

        return new ArrayList<>(grouped.values());
    }
}
