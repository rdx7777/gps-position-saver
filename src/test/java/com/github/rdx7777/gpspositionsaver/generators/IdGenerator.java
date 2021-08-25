package com.github.rdx7777.gpspositionsaver.generators;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static AtomicLong atomicLong = new AtomicLong(0);

    public static Long getNextId() {
        return atomicLong.incrementAndGet();
    }
}
