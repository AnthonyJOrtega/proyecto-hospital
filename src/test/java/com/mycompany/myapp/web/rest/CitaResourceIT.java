package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CitaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.domain.enumeration.EstadoCita;
import com.mycompany.myapp.repository.CitaRepository;
import com.mycompany.myapp.service.CitaService;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.mapper.CitaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CitaResourceIT {

    private static final LocalDate DEFAULT_FECHA_CREACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CREACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_CREACION = LocalDate.ofEpochDay(-1L);

    private static final EstadoCita DEFAULT_ESTADO_CITA = EstadoCita.FINALIZADO;
    private static final EstadoCita UPDATED_ESTADO_CITA = EstadoCita.PENDIENTE;

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/citas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitaRepository citaRepository;

    @Mock
    private CitaRepository citaRepositoryMock;

    @Autowired
    private CitaMapper citaMapper;

    @Mock
    private CitaService citaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaMockMvc;

    private Cita cita;

    private Cita insertedCita;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity() {
        return new Cita().fechaCreacion(DEFAULT_FECHA_CREACION).estadoCita(DEFAULT_ESTADO_CITA).observaciones(DEFAULT_OBSERVACIONES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createUpdatedEntity() {
        return new Cita().fechaCreacion(UPDATED_FECHA_CREACION).estadoCita(UPDATED_ESTADO_CITA).observaciones(UPDATED_OBSERVACIONES);
    }

    @BeforeEach
    void initTest() {
        cita = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCita != null) {
            citaRepository.delete(insertedCita);
            insertedCita = null;
        }
    }

    @Test
    @Transactional
    void createCita() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);
        var returnedCitaDTO = om.readValue(
            restCitaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CitaDTO.class
        );

        // Validate the Cita in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCita = citaMapper.toEntity(returnedCitaDTO);
        assertCitaUpdatableFieldsEquals(returnedCita, getPersistedCita(returnedCita));

        insertedCita = returnedCita;
    }

    @Test
    @Transactional
    void createCitaWithExistingId() throws Exception {
        // Create the Cita with an existing ID
        cita.setId(1L);
        CitaDTO citaDTO = citaMapper.toDto(cita);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitas() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].estadoCita").value(hasItem(DEFAULT_ESTADO_CITA.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitasWithEagerRelationshipsIsEnabled() throws Exception {
        when(citaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(citaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(citaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(citaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get the cita
        restCitaMockMvc
            .perform(get(ENTITY_API_URL_ID, cita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.estadoCita").value(DEFAULT_ESTADO_CITA.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getCitasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        Long id = cita.getId();

        defaultCitaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCitaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCitaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion equals to
        defaultCitaFiltering("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION, "fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion in
        defaultCitaFiltering(
            "fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION,
            "fechaCreacion.in=" + UPDATED_FECHA_CREACION
        );
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion is not null
        defaultCitaFiltering("fechaCreacion.specified=true", "fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion is greater than or equal to
        defaultCitaFiltering(
            "fechaCreacion.greaterThanOrEqual=" + DEFAULT_FECHA_CREACION,
            "fechaCreacion.greaterThanOrEqual=" + UPDATED_FECHA_CREACION
        );
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion is less than or equal to
        defaultCitaFiltering(
            "fechaCreacion.lessThanOrEqual=" + DEFAULT_FECHA_CREACION,
            "fechaCreacion.lessThanOrEqual=" + SMALLER_FECHA_CREACION
        );
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion is less than
        defaultCitaFiltering("fechaCreacion.lessThan=" + UPDATED_FECHA_CREACION, "fechaCreacion.lessThan=" + DEFAULT_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllCitasByFechaCreacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where fechaCreacion is greater than
        defaultCitaFiltering("fechaCreacion.greaterThan=" + SMALLER_FECHA_CREACION, "fechaCreacion.greaterThan=" + DEFAULT_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllCitasByEstadoCitaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where estadoCita equals to
        defaultCitaFiltering("estadoCita.equals=" + DEFAULT_ESTADO_CITA, "estadoCita.equals=" + UPDATED_ESTADO_CITA);
    }

    @Test
    @Transactional
    void getAllCitasByEstadoCitaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where estadoCita in
        defaultCitaFiltering("estadoCita.in=" + DEFAULT_ESTADO_CITA + "," + UPDATED_ESTADO_CITA, "estadoCita.in=" + UPDATED_ESTADO_CITA);
    }

    @Test
    @Transactional
    void getAllCitasByEstadoCitaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where estadoCita is not null
        defaultCitaFiltering("estadoCita.specified=true", "estadoCita.specified=false");
    }

    @Test
    @Transactional
    void getAllCitasByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where observaciones equals to
        defaultCitaFiltering("observaciones.equals=" + DEFAULT_OBSERVACIONES, "observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCitasByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where observaciones in
        defaultCitaFiltering(
            "observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES,
            "observaciones.in=" + UPDATED_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllCitasByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where observaciones is not null
        defaultCitaFiltering("observaciones.specified=true", "observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllCitasByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where observaciones contains
        defaultCitaFiltering("observaciones.contains=" + DEFAULT_OBSERVACIONES, "observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCitasByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList where observaciones does not contain
        defaultCitaFiltering(
            "observaciones.doesNotContain=" + UPDATED_OBSERVACIONES,
            "observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllCitasByInformeIsEqualToSomething() throws Exception {
        Informe informe;
        if (TestUtil.findAll(em, Informe.class).isEmpty()) {
            citaRepository.saveAndFlush(cita);
            informe = InformeResourceIT.createEntity();
        } else {
            informe = TestUtil.findAll(em, Informe.class).get(0);
        }
        em.persist(informe);
        em.flush();
        cita.setInforme(informe);
        citaRepository.saveAndFlush(cita);
        Long informeId = informe.getId();
        // Get all the citaList where informe equals to informeId
        defaultCitaShouldBeFound("informeId.equals=" + informeId);

        // Get all the citaList where informe equals to (informeId + 1)
        defaultCitaShouldNotBeFound("informeId.equals=" + (informeId + 1));
    }

    @Test
    @Transactional
    void getAllCitasByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            citaRepository.saveAndFlush(cita);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        cita.setPaciente(paciente);
        citaRepository.saveAndFlush(cita);
        Long pacienteId = paciente.getId();
        // Get all the citaList where paciente equals to pacienteId
        defaultCitaShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the citaList where paciente equals to (pacienteId + 1)
        defaultCitaShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllCitasByTrabajadorIsEqualToSomething() throws Exception {
        Trabajador trabajador;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            citaRepository.saveAndFlush(cita);
            trabajador = TrabajadorResourceIT.createEntity();
        } else {
            trabajador = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajador);
        em.flush();
        cita.addTrabajador(trabajador);
        citaRepository.saveAndFlush(cita);
        Long trabajadorId = trabajador.getId();
        // Get all the citaList where trabajador equals to trabajadorId
        defaultCitaShouldBeFound("trabajadorId.equals=" + trabajadorId);

        // Get all the citaList where trabajador equals to (trabajadorId + 1)
        defaultCitaShouldNotBeFound("trabajadorId.equals=" + (trabajadorId + 1));
    }

    private void defaultCitaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCitaShouldBeFound(shouldBeFound);
        defaultCitaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCitaShouldBeFound(String filter) throws Exception {
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].estadoCita").value(hasItem(DEFAULT_ESTADO_CITA.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCitaShouldNotBeFound(String filter) throws Exception {
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCita() throws Exception {
        // Get the cita
        restCitaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita
        Cita updatedCita = citaRepository.findById(cita.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCita are not directly saved in db
        em.detach(updatedCita);
        updatedCita.fechaCreacion(UPDATED_FECHA_CREACION).estadoCita(UPDATED_ESTADO_CITA).observaciones(UPDATED_OBSERVACIONES);
        CitaDTO citaDTO = citaMapper.toDto(updatedCita);

        restCitaMockMvc
            .perform(put(ENTITY_API_URL_ID, citaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isOk());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitaToMatchAllProperties(updatedCita);
    }

    @Test
    @Transactional
    void putNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(put(ENTITY_API_URL_ID, citaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaCreacion(UPDATED_FECHA_CREACION).observaciones(UPDATED_OBSERVACIONES);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCita, cita), getPersistedCita(cita));
    }

    @Test
    @Transactional
    void fullUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaCreacion(UPDATED_FECHA_CREACION).estadoCita(UPDATED_ESTADO_CITA).observaciones(UPDATED_OBSERVACIONES);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(partialUpdatedCita, getPersistedCita(partialUpdatedCita));
    }

    @Test
    @Transactional
    void patchNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citaDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cita
        restCitaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cita.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Cita getPersistedCita(Cita cita) {
        return citaRepository.findById(cita.getId()).orElseThrow();
    }

    protected void assertPersistedCitaToMatchAllProperties(Cita expectedCita) {
        assertCitaAllPropertiesEquals(expectedCita, getPersistedCita(expectedCita));
    }

    protected void assertPersistedCitaToMatchUpdatableProperties(Cita expectedCita) {
        assertCitaAllUpdatablePropertiesEquals(expectedCita, getPersistedCita(expectedCita));
    }
}
