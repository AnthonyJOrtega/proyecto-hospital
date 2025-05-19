package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrabajadorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Trabajador getTrabajadorSample1() {
        return new Trabajador().id(1L).idUsuario(1L).nombre("nombre1").apellido("apellido1").dni("dni1");
    }

    public static Trabajador getTrabajadorSample2() {
        return new Trabajador().id(2L).idUsuario(2L).nombre("nombre2").apellido("apellido2").dni("dni2");
    }

    public static Trabajador getTrabajadorRandomSampleGenerator() {
        return new Trabajador()
            .id(longCount.incrementAndGet())
            .idUsuario(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .dni(UUID.randomUUID().toString());
    }
}
