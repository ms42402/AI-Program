package com.visualizer.visualizationapp;

import java.time.Duration;
import java.time.Instant;

public class Benchmarker {
    private Instant _start;
    private Instant _stop;
    private int _numOfStops;
    private long _elapsedTime;

    public Benchmarker() {
        _elapsedTime = 0;
        _numOfStops = 0;
    }

    public void start() {
        _start = Instant.now();
    }

    public void stop() {
        _stop = Instant.now();
        _numOfStops++;
        _elapsedTime += Duration.between(_start, _stop).toNanos();
    }

    public long averageTimeInMillis() {
        return (_elapsedTime / 1000000) / _numOfStops;
    }

    public long averageTimeInNanos() {
        return _elapsedTime / _numOfStops;
    }

}
