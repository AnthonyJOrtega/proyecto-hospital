package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DireccionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.service.DireccionService;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.mapper.DireccionMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIDAD = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGO_POSTAL = 1;
    private static final Integer UPDATED_CODIGO_POSTAL = 2;
    private static final Integer SMALLER_CODIGO_POSTAL = 1 - 1;

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DireccionRepository direccionRepository;

    @Mock
    private DireccionRepository direccionRepositoryMock;

    @Autowired
    private DireccionMapper direccionMapper;

    @Mock
    private DireccionService direccionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    private Direccion insertedDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity() {
        return new Direccion()
            .pais(DEFAULT_PAIS)
            .ciudad(DEFAULT_CIUDAD)
            .localidad(DEFAULT_LOCALIDAD)
            .codigoPostal(DEFAULT_CODIGO_POSTAL)
            .calle(DEFAULT_CALLE)
            .numero(DEFAULT_NUMERO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity() {
        return new Direccion()
            .pais(UPDATED_PAIS)
            .ciudad(UPDATED_CIUDAD)
            .localidad(UPDATED_LOCALIDAD)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .calle(UPDATED_CALLE)
            .numero(UPDATED_NUMERO);
    }

    @BeforeEach
    void initTest() {
        direccion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDireccion != null) {
            direccionRepository.delete(insertedDireccion);
            insertedDireccion = null;
        }
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);
        var returnedDireccionDTO = om.readValue(
            restDireccionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DireccionDTO.class
        );

        // Validate the Direccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDireccion = direccionMapper.toEntity(returnedDireccionDTO);
        assertDireccionUpdatableFieldsEquals(returnedDireccion, getPersistedDireccion(returnedDireccion));

        insertedDireccion = returnedDireccion;
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].localidad").value(hasItem(DEFAULT_LOCALIDAD)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(direccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDireccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(direccionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(direccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDireccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(direccionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.localidad").value(DEFAULT_LOCALIDAD))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    void getDireccionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        Long id = direccion.getId();

        defaultDireccionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDireccionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDireccionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDireccionsByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where pais equals to
        defaultDireccionFiltering("pais.equals=" + DEFAULT_PAIS, "pais.equals=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllDireccionsByPaisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where pais in
        defaultDireccionFiltering("pais.in=" + DEFAULT_PAIS + "," + UPDATED_PAIS, "pais.in=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllDireccionsByPaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where pais is not null
        defaultDireccionFiltering("pais.specified=true", "pais.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByPaisContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where pais contains
        defaultDireccionFiltering("pais.contains=" + DEFAULT_PAIS, "pais.contains=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllDireccionsByPaisNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where pais does not contain
        defaultDireccionFiltering("pais.doesNotContain=" + UPDATED_PAIS, "pais.doesNotContain=" + DEFAULT_PAIS);
    }

    @Test
    @Transactional
    void getAllDireccionsByCiudadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where ciudad equals to
        defaultDireccionFiltering("ciudad.equals=" + DEFAULT_CIUDAD, "ciudad.equals=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByCiudadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where ciudad in
        defaultDireccionFiltering("ciudad.in=" + DEFAULT_CIUDAD + "," + UPDATED_CIUDAD, "ciudad.in=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByCiudadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where ciudad is not null
        defaultDireccionFiltering("ciudad.specified=true", "ciudad.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByCiudadContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where ciudad contains
        defaultDireccionFiltering("ciudad.contains=" + DEFAULT_CIUDAD, "ciudad.contains=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByCiudadNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where ciudad does not contain
        defaultDireccionFiltering("ciudad.doesNotContain=" + UPDATED_CIUDAD, "ciudad.doesNotContain=" + DEFAULT_CIUDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLocalidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where localidad equals to
        defaultDireccionFiltering("localidad.equals=" + DEFAULT_LOCALIDAD, "localidad.equals=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLocalidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where localidad in
        defaultDireccionFiltering("localidad.in=" + DEFAULT_LOCALIDAD + "," + UPDATED_LOCALIDAD, "localidad.in=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLocalidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where localidad is not null
        defaultDireccionFiltering("localidad.specified=true", "localidad.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByLocalidadContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where localidad contains
        defaultDireccionFiltering("localidad.contains=" + DEFAULT_LOCALIDAD, "localidad.contains=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLocalidadNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where localidad does not contain
        defaultDireccionFiltering("localidad.doesNotContain=" + UPDATED_LOCALIDAD, "localidad.doesNotContain=" + DEFAULT_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal equals to
        defaultDireccionFiltering("codigoPostal.equals=" + DEFAULT_CODIGO_POSTAL, "codigoPostal.equals=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal in
        defaultDireccionFiltering(
            "codigoPostal.in=" + DEFAULT_CODIGO_POSTAL + "," + UPDATED_CODIGO_POSTAL,
            "codigoPostal.in=" + UPDATED_CODIGO_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal is not null
        defaultDireccionFiltering("codigoPostal.specified=true", "codigoPostal.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal is greater than or equal to
        defaultDireccionFiltering(
            "codigoPostal.greaterThanOrEqual=" + DEFAULT_CODIGO_POSTAL,
            "codigoPostal.greaterThanOrEqual=" + UPDATED_CODIGO_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal is less than or equal to
        defaultDireccionFiltering(
            "codigoPostal.lessThanOrEqual=" + DEFAULT_CODIGO_POSTAL,
            "codigoPostal.lessThanOrEqual=" + SMALLER_CODIGO_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal is less than
        defaultDireccionFiltering("codigoPostal.lessThan=" + UPDATED_CODIGO_POSTAL, "codigoPostal.lessThan=" + DEFAULT_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    void getAllDireccionsByCodigoPostalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where codigoPostal is greater than
        defaultDireccionFiltering("codigoPostal.greaterThan=" + SMALLER_CODIGO_POSTAL, "codigoPostal.greaterThan=" + DEFAULT_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    void getAllDireccionsByCalleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where calle equals to
        defaultDireccionFiltering("calle.equals=" + DEFAULT_CALLE, "calle.equals=" + UPDATED_CALLE);
    }

    @Test
    @Transactional
    void getAllDireccionsByCalleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where calle in
        defaultDireccionFiltering("calle.in=" + DEFAULT_CALLE + "," + UPDATED_CALLE, "calle.in=" + UPDATED_CALLE);
    }

    @Test
    @Transactional
    void getAllDireccionsByCalleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where calle is not null
        defaultDireccionFiltering("calle.specified=true", "calle.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByCalleContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where calle contains
        defaultDireccionFiltering("calle.contains=" + DEFAULT_CALLE, "calle.contains=" + UPDATED_CALLE);
    }

    @Test
    @Transactional
    void getAllDireccionsByCalleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where calle does not contain
        defaultDireccionFiltering("calle.doesNotContain=" + UPDATED_CALLE, "calle.doesNotContain=" + DEFAULT_CALLE);
    }

    @Test
    @Transactional
    void getAllDireccionsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero equals to
        defaultDireccionFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDireccionsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero in
        defaultDireccionFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDireccionsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is not null
        defaultDireccionFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero contains
        defaultDireccionFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDireccionsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero does not contain
        defaultDireccionFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllDireccionsByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            direccionRepository.saveAndFlush(direccion);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        direccion.addPaciente(paciente);
        direccionRepository.saveAndFlush(direccion);
        Long pacienteId = paciente.getId();
        // Get all the direccionList where paciente equals to pacienteId
        defaultDireccionShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the direccionList where paciente equals to (pacienteId + 1)
        defaultDireccionShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllDireccionsByTrabajadorIsEqualToSomething() throws Exception {
        Trabajador trabajador;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            direccionRepository.saveAndFlush(direccion);
            trabajador = TrabajadorResourceIT.createEntity();
        } else {
            trabajador = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajador);
        em.flush();
        direccion.addTrabajador(trabajador);
        direccionRepository.saveAndFlush(direccion);
        Long trabajadorId = trabajador.getId();
        // Get all the direccionList where trabajador equals to trabajadorId
        defaultDireccionShouldBeFound("trabajadorId.equals=" + trabajadorId);

        // Get all the direccionList where trabajador equals to (trabajadorId + 1)
        defaultDireccionShouldNotBeFound("trabajadorId.equals=" + (trabajadorId + 1));
    }

    private void defaultDireccionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDireccionShouldBeFound(shouldBeFound);
        defaultDireccionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDireccionShouldBeFound(String filter) throws Exception {
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].localidad").value(hasItem(DEFAULT_LOCALIDAD)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));

        // Check, that the count call also returns 1
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDireccionShouldNotBeFound(String filter) throws Exception {
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion
            .pais(UPDATED_PAIS)
            .ciudad(UPDATED_CIUDAD)
            .localidad(UPDATED_LOCALIDAD)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .calle(UPDATED_CALLE)
            .numero(UPDATED_NUMERO);
        DireccionDTO direccionDTO = direccionMapper.toDto(updatedDireccion);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDireccionToMatchAllProperties(updatedDireccion);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.pais(UPDATED_PAIS).localidad(UPDATED_LOCALIDAD).calle(UPDATED_CALLE).numero(UPDATED_NUMERO);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDireccion, direccion),
            getPersistedDireccion(direccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .pais(UPDATED_PAIS)
            .ciudad(UPDATED_CIUDAD)
            .localidad(UPDATED_LOCALIDAD)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .calle(UPDATED_CALLE)
            .numero(UPDATED_NUMERO);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(partialUpdatedDireccion, getPersistedDireccion(partialUpdatedDireccion));
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return direccionRepository.count();
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

    protected Direccion getPersistedDireccion(Direccion direccion) {
        return direccionRepository.findById(direccion.getId()).orElseThrow();
    }

    protected void assertPersistedDireccionToMatchAllProperties(Direccion expectedDireccion) {
        assertDireccionAllPropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }

    protected void assertPersistedDireccionToMatchUpdatableProperties(Direccion expectedDireccion) {
        assertDireccionAllUpdatablePropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }
}
