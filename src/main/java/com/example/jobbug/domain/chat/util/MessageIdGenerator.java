package com.example.jobbug.domain.chat.util;

import org.springframework.stereotype.Component;

@Component
public class MessageIdGenerator {

    private static final long MAX_SEQUENCE = 9999L;

    private long lastTimestamp = -1L;
    private long sequence = 0;

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp == lastTimestamp) {
            sequence++;
            if (sequence > MAX_SEQUENCE) {
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
                sequence = 0;
            }
        } else {
            sequence = 0;
            lastTimestamp = timestamp;
        }

        return (timestamp * 10000) + sequence;
    }
}
