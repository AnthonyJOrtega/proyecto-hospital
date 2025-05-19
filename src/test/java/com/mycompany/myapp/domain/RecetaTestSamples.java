package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Receta getRecetaSample1() {
        return new Receta().id(1L).instrucciones("instrucciones1");
    }

    public static Receta getRecetaSample2() {
        return new Receta().id(2L).instrucciones("instrucciones2");
    }

    public static Receta getRecetaRandomSampleGenerator() {
        return new Receta().id(longCount.incrementAndGet()).instrucciones(UUID.randomUUID().toString());
    }
}
