package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .dni("dni1")
            .seguroMedico("seguroMedico1")
            .telefono("telefono1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .dni("dni2")
            .seguroMedico("seguroMedico2")
            .telefono("telefono2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .dni(UUID.randomUUID().toString())
            .seguroMedico(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
