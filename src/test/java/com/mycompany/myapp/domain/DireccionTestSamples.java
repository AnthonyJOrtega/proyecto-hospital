package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Direccion getDireccionSample1() {
        return new Direccion()
            .id(1L)
            .pais("pais1")
            .ciudad("ciudad1")
            .localidad("localidad1")
            .codigoPostal(1)
            .calle("calle1")
            .numero("numero1");
    }

    public static Direccion getDireccionSample2() {
        return new Direccion()
            .id(2L)
            .pais("pais2")
            .ciudad("ciudad2")
            .localidad("localidad2")
            .codigoPostal(2)
            .calle("calle2")
            .numero("numero2");
    }

    public static Direccion getDireccionRandomSampleGenerator() {
        return new Direccion()
            .id(longCount.incrementAndGet())
            .pais(UUID.randomUUID().toString())
            .ciudad(UUID.randomUUID().toString())
            .localidad(UUID.randomUUID().toString())
            .codigoPostal(intCount.incrementAndGet())
            .calle(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString());
    }
}
