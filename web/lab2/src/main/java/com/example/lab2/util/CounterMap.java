package com.example.lab2.util;

import java.util.concurrent.ConcurrentHashMap;

public class CounterMap {
    private final ConcurrentHashMap<String, Long> headerCountMap = new ConcurrentHashMap<>();

    public void incrementHeader(String headerName) {
        headerCountMap.merge(headerName, 1L, Long::sum);
    }

    public ConcurrentHashMap<String, Long> getHeaderCountMap() {
        return headerCountMap;
    }
}
