package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnfermedadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enfermedad getEnfermedadSample1() {
        return new Enfermedad().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Enfermedad getEnfermedadSample2() {
        return new Enfermedad().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Enfermedad getEnfermedadRandomSampleGenerator() {
        return new Enfermedad()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
