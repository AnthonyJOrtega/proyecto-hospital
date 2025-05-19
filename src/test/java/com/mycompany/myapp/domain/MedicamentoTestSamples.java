package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medicamento getMedicamentoSample1() {
        return new Medicamento().id(1L).nombre("nombre1").descripcion("descripcion1").dosis("dosis1");
    }

    public static Medicamento getMedicamentoSample2() {
        return new Medicamento().id(2L).nombre("nombre2").descripcion("descripcion2").dosis("dosis2");
    }

    public static Medicamento getMedicamentoRandomSampleGenerator() {
        return new Medicamento()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .dosis(UUID.randomUUID().toString());
    }
}
