package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RecetaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.RecetaRepository;
import com.mycompany.myapp.service.RecetaService;
import com.mycompany.myapp.service.dto.RecetaDTO;
import com.mycompany.myapp.service.mapper.RecetaMapper;
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
 * Integration tests for the {@link RecetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecetaResourceIT {

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_INSTRUCCIONES = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCCIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecetaRepository recetaRepository;

    @Mock
    private RecetaRepository recetaRepositoryMock;

    @Autowired
    private RecetaMapper recetaMapper;

    @Mock
    private RecetaService recetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecetaMockMvc;

    private Receta receta;

    private Receta insertedReceta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receta createEntity() {
        return new Receta().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).instrucciones(DEFAULT_INSTRUCCIONES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receta createUpdatedEntity() {
        return new Receta().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).instrucciones(UPDATED_INSTRUCCIONES);
    }

    @BeforeEach
    void initTest() {
        receta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReceta != null) {
            recetaRepository.delete(insertedReceta);
            insertedReceta = null;
        }
    }

    @Test
    @Transactional
    void createReceta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);
        var returnedRecetaDTO = om.readValue(
            restRecetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecetaDTO.class
        );

        // Validate the Receta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReceta = recetaMapper.toEntity(returnedRecetaDTO);
        assertRecetaUpdatableFieldsEquals(returnedReceta, getPersistedReceta(returnedReceta));

        insertedReceta = returnedReceta;
    }

    @Test
    @Transactional
    void createRecetaWithExistingId() throws Exception {
        // Create the Receta with an existing ID
        receta.setId(1L);
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecetas() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].instrucciones").value(hasItem(DEFAULT_INSTRUCCIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(recetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReceta() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get the receta
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL_ID, receta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receta.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.instrucciones").value(DEFAULT_INSTRUCCIONES));
    }

    @Test
    @Transactional
    void getRecetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        Long id = receta.getId();

        defaultRecetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio equals to
        defaultRecetaFiltering("fechaInicio.equals=" + DEFAULT_FECHA_INICIO, "fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio in
        defaultRecetaFiltering(
            "fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO,
            "fechaInicio.in=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio is not null
        defaultRecetaFiltering("fechaInicio.specified=true", "fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio is greater than or equal to
        defaultRecetaFiltering(
            "fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio is less than or equal to
        defaultRecetaFiltering(
            "fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio is less than
        defaultRecetaFiltering("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO, "fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaInicio is greater than
        defaultRecetaFiltering("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO, "fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin equals to
        defaultRecetaFiltering("fechaFin.equals=" + DEFAULT_FECHA_FIN, "fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin in
        defaultRecetaFiltering("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN, "fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin is not null
        defaultRecetaFiltering("fechaFin.specified=true", "fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin is greater than or equal to
        defaultRecetaFiltering("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin is less than or equal to
        defaultRecetaFiltering("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin is less than
        defaultRecetaFiltering("fechaFin.lessThan=" + UPDATED_FECHA_FIN, "fechaFin.lessThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where fechaFin is greater than
        defaultRecetaFiltering("fechaFin.greaterThan=" + SMALLER_FECHA_FIN, "fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllRecetasByInstruccionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where instrucciones equals to
        defaultRecetaFiltering("instrucciones.equals=" + DEFAULT_INSTRUCCIONES, "instrucciones.equals=" + UPDATED_INSTRUCCIONES);
    }

    @Test
    @Transactional
    void getAllRecetasByInstruccionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where instrucciones in
        defaultRecetaFiltering(
            "instrucciones.in=" + DEFAULT_INSTRUCCIONES + "," + UPDATED_INSTRUCCIONES,
            "instrucciones.in=" + UPDATED_INSTRUCCIONES
        );
    }

    @Test
    @Transactional
    void getAllRecetasByInstruccionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where instrucciones is not null
        defaultRecetaFiltering("instrucciones.specified=true", "instrucciones.specified=false");
    }

    @Test
    @Transactional
    void getAllRecetasByInstruccionesContainsSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where instrucciones contains
        defaultRecetaFiltering("instrucciones.contains=" + DEFAULT_INSTRUCCIONES, "instrucciones.contains=" + UPDATED_INSTRUCCIONES);
    }

    @Test
    @Transactional
    void getAllRecetasByInstruccionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        // Get all the recetaList where instrucciones does not contain
        defaultRecetaFiltering(
            "instrucciones.doesNotContain=" + UPDATED_INSTRUCCIONES,
            "instrucciones.doesNotContain=" + DEFAULT_INSTRUCCIONES
        );
    }

    @Test
    @Transactional
    void getAllRecetasByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            recetaRepository.saveAndFlush(receta);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        receta.setPaciente(paciente);
        recetaRepository.saveAndFlush(receta);
        Long pacienteId = paciente.getId();
        // Get all the recetaList where paciente equals to pacienteId
        defaultRecetaShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the recetaList where paciente equals to (pacienteId + 1)
        defaultRecetaShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllRecetasByTrabajadorIsEqualToSomething() throws Exception {
        Trabajador trabajador;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            recetaRepository.saveAndFlush(receta);
            trabajador = TrabajadorResourceIT.createEntity();
        } else {
            trabajador = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajador);
        em.flush();
        receta.setTrabajador(trabajador);
        recetaRepository.saveAndFlush(receta);
        Long trabajadorId = trabajador.getId();
        // Get all the recetaList where trabajador equals to trabajadorId
        defaultRecetaShouldBeFound("trabajadorId.equals=" + trabajadorId);

        // Get all the recetaList where trabajador equals to (trabajadorId + 1)
        defaultRecetaShouldNotBeFound("trabajadorId.equals=" + (trabajadorId + 1));
    }

    @Test
    @Transactional
    void getAllRecetasByMedicamentoIsEqualToSomething() throws Exception {
        Medicamento medicamento;
        if (TestUtil.findAll(em, Medicamento.class).isEmpty()) {
            recetaRepository.saveAndFlush(receta);
            medicamento = MedicamentoResourceIT.createEntity();
        } else {
            medicamento = TestUtil.findAll(em, Medicamento.class).get(0);
        }
        em.persist(medicamento);
        em.flush();
        receta.addMedicamento(medicamento);
        recetaRepository.saveAndFlush(receta);
        Long medicamentoId = medicamento.getId();
        // Get all the recetaList where medicamento equals to medicamentoId
        defaultRecetaShouldBeFound("medicamentoId.equals=" + medicamentoId);

        // Get all the recetaList where medicamento equals to (medicamentoId + 1)
        defaultRecetaShouldNotBeFound("medicamentoId.equals=" + (medicamentoId + 1));
    }

    private void defaultRecetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecetaShouldBeFound(shouldBeFound);
        defaultRecetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecetaShouldBeFound(String filter) throws Exception {
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].instrucciones").value(hasItem(DEFAULT_INSTRUCCIONES)));

        // Check, that the count call also returns 1
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecetaShouldNotBeFound(String filter) throws Exception {
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReceta() throws Exception {
        // Get the receta
        restRecetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReceta() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receta
        Receta updatedReceta = recetaRepository.findById(receta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReceta are not directly saved in db
        em.detach(updatedReceta);
        updatedReceta.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).instrucciones(UPDATED_INSTRUCCIONES);
        RecetaDTO recetaDTO = recetaMapper.toDto(updatedReceta);

        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recetaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecetaToMatchAllProperties(updatedReceta);
    }

    @Test
    @Transactional
    void putNonExistingReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recetaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecetaWithPatch() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receta using partial update
        Receta partialUpdatedReceta = new Receta();
        partialUpdatedReceta.setId(receta.getId());

        partialUpdatedReceta.fechaInicio(UPDATED_FECHA_INICIO);

        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReceta))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecetaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReceta, receta), getPersistedReceta(receta));
    }

    @Test
    @Transactional
    void fullUpdateRecetaWithPatch() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receta using partial update
        Receta partialUpdatedReceta = new Receta();
        partialUpdatedReceta.setId(receta.getId());

        partialUpdatedReceta.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).instrucciones(UPDATED_INSTRUCCIONES);

        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReceta))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecetaUpdatableFieldsEquals(partialUpdatedReceta, getPersistedReceta(partialUpdatedReceta));
    }

    @Test
    @Transactional
    void patchNonExistingReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receta.setId(longCount.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceta() throws Exception {
        // Initialize the database
        insertedReceta = recetaRepository.saveAndFlush(receta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the receta
        restRecetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, receta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recetaRepository.count();
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

    protected Receta getPersistedReceta(Receta receta) {
        return recetaRepository.findById(receta.getId()).orElseThrow();
    }

    protected void assertPersistedRecetaToMatchAllProperties(Receta expectedReceta) {
        assertRecetaAllPropertiesEquals(expectedReceta, getPersistedReceta(expectedReceta));
    }

    protected void assertPersistedRecetaToMatchUpdatableProperties(Receta expectedReceta) {
        assertRecetaAllUpdatablePropertiesEquals(expectedReceta, getPersistedReceta(expectedReceta));
    }
}
