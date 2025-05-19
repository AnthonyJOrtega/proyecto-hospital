package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InformeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Informe getInformeSample1() {
        return new Informe().id(1L).fecha("fecha1").resumen("resumen1");
    }

    public static Informe getInformeSample2() {
        return new Informe().id(2L).fecha("fecha2").resumen("resumen2");
    }

    public static Informe getInformeRandomSampleGenerator() {
        return new Informe().id(longCount.incrementAndGet()).fecha(UUID.randomUUID().toString()).resumen(UUID.randomUUID().toString());
    }
}
