package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.TrabajadorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Especialidad;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.domain.enumeration.Puesto;
import com.mycompany.myapp.domain.enumeration.Turno;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.service.TrabajadorService;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import com.mycompany.myapp.service.mapper.TrabajadorMapper;
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
 * Integration tests for the {@link TrabajadorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrabajadorResourceIT {

    private static final Long DEFAULT_ID_USUARIO = 1L;
    private static final Long UPDATED_ID_USUARIO = 2L;
    private static final Long SMALLER_ID_USUARIO = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final Puesto DEFAULT_PUESTO = Puesto.MEDICO;
    private static final Puesto UPDATED_PUESTO = Puesto.ENFERMERO;

    private static final Boolean DEFAULT_DISPONIBILIDAD = false;
    private static final Boolean UPDATED_DISPONIBILIDAD = true;

    private static final Turno DEFAULT_TURNO = Turno.DIA;
    private static final Turno UPDATED_TURNO = Turno.TARDE;

    private static final String ENTITY_API_URL = "/api/trabajadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Mock
    private TrabajadorRepository trabajadorRepositoryMock;

    @Autowired
    private TrabajadorMapper trabajadorMapper;

    @Mock
    private TrabajadorService trabajadorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrabajadorMockMvc;

    private Trabajador trabajador;

    private Trabajador insertedTrabajador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajador createEntity() {
        return new Trabajador()
            .idUsuario(DEFAULT_ID_USUARIO)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .dni(DEFAULT_DNI)
            .puesto(DEFAULT_PUESTO)
            .disponibilidad(DEFAULT_DISPONIBILIDAD)
            .turno(DEFAULT_TURNO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajador createUpdatedEntity() {
        return new Trabajador()
            .idUsuario(UPDATED_ID_USUARIO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dni(UPDATED_DNI)
            .puesto(UPDATED_PUESTO)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .turno(UPDATED_TURNO);
    }

    @BeforeEach
    void initTest() {
        trabajador = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrabajador != null) {
            trabajadorRepository.delete(insertedTrabajador);
            insertedTrabajador = null;
        }
    }

    @Test
    @Transactional
    void createTrabajador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);
        var returnedTrabajadorDTO = om.readValue(
            restTrabajadorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrabajadorDTO.class
        );

        // Validate the Trabajador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrabajador = trabajadorMapper.toEntity(returnedTrabajadorDTO);
        assertTrabajadorUpdatableFieldsEquals(returnedTrabajador, getPersistedTrabajador(returnedTrabajador));

        insertedTrabajador = returnedTrabajador;
    }

    @Test
    @Transactional
    void createTrabajadorWithExistingId() throws Exception {
        // Create the Trabajador with an existing ID
        trabajador.setId(1L);
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrabajadors() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUsuario").value(hasItem(DEFAULT_ID_USUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].puesto").value(hasItem(DEFAULT_PUESTO.toString())))
            .andExpect(jsonPath("$.[*].disponibilidad").value(hasItem(DEFAULT_DISPONIBILIDAD)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajadorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(trabajadorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajadorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trabajadorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajadorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trabajadorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajadorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trabajadorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get the trabajador
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL_ID, trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trabajador.getId().intValue()))
            .andExpect(jsonPath("$.idUsuario").value(DEFAULT_ID_USUARIO.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.puesto").value(DEFAULT_PUESTO.toString()))
            .andExpect(jsonPath("$.disponibilidad").value(DEFAULT_DISPONIBILIDAD))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO.toString()));
    }

    @Test
    @Transactional
    void getTrabajadorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        Long id = trabajador.getId();

        defaultTrabajadorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrabajadorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrabajadorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario equals to
        defaultTrabajadorFiltering("idUsuario.equals=" + DEFAULT_ID_USUARIO, "idUsuario.equals=" + UPDATED_ID_USUARIO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario in
        defaultTrabajadorFiltering("idUsuario.in=" + DEFAULT_ID_USUARIO + "," + UPDATED_ID_USUARIO, "idUsuario.in=" + UPDATED_ID_USUARIO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario is not null
        defaultTrabajadorFiltering("idUsuario.specified=true", "idUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario is greater than or equal to
        defaultTrabajadorFiltering(
            "idUsuario.greaterThanOrEqual=" + DEFAULT_ID_USUARIO,
            "idUsuario.greaterThanOrEqual=" + UPDATED_ID_USUARIO
        );
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario is less than or equal to
        defaultTrabajadorFiltering("idUsuario.lessThanOrEqual=" + DEFAULT_ID_USUARIO, "idUsuario.lessThanOrEqual=" + SMALLER_ID_USUARIO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario is less than
        defaultTrabajadorFiltering("idUsuario.lessThan=" + UPDATED_ID_USUARIO, "idUsuario.lessThan=" + DEFAULT_ID_USUARIO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByIdUsuarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where idUsuario is greater than
        defaultTrabajadorFiltering("idUsuario.greaterThan=" + SMALLER_ID_USUARIO, "idUsuario.greaterThan=" + DEFAULT_ID_USUARIO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre equals to
        defaultTrabajadorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre in
        defaultTrabajadorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre is not null
        defaultTrabajadorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre contains
        defaultTrabajadorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre does not contain
        defaultTrabajadorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where apellido equals to
        defaultTrabajadorFiltering("apellido.equals=" + DEFAULT_APELLIDO, "apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where apellido in
        defaultTrabajadorFiltering("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO, "apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where apellido is not null
        defaultTrabajadorFiltering("apellido.specified=true", "apellido.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByApellidoContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where apellido contains
        defaultTrabajadorFiltering("apellido.contains=" + DEFAULT_APELLIDO, "apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where apellido does not contain
        defaultTrabajadorFiltering("apellido.doesNotContain=" + UPDATED_APELLIDO, "apellido.doesNotContain=" + DEFAULT_APELLIDO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where dni equals to
        defaultTrabajadorFiltering("dni.equals=" + DEFAULT_DNI, "dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where dni in
        defaultTrabajadorFiltering("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI, "dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where dni is not null
        defaultTrabajadorFiltering("dni.specified=true", "dni.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDniContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where dni contains
        defaultTrabajadorFiltering("dni.contains=" + DEFAULT_DNI, "dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where dni does not contain
        defaultTrabajadorFiltering("dni.doesNotContain=" + UPDATED_DNI, "dni.doesNotContain=" + DEFAULT_DNI);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByPuestoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where puesto equals to
        defaultTrabajadorFiltering("puesto.equals=" + DEFAULT_PUESTO, "puesto.equals=" + UPDATED_PUESTO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByPuestoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where puesto in
        defaultTrabajadorFiltering("puesto.in=" + DEFAULT_PUESTO + "," + UPDATED_PUESTO, "puesto.in=" + UPDATED_PUESTO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByPuestoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where puesto is not null
        defaultTrabajadorFiltering("puesto.specified=true", "puesto.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDisponibilidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where disponibilidad equals to
        defaultTrabajadorFiltering("disponibilidad.equals=" + DEFAULT_DISPONIBILIDAD, "disponibilidad.equals=" + UPDATED_DISPONIBILIDAD);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDisponibilidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where disponibilidad in
        defaultTrabajadorFiltering(
            "disponibilidad.in=" + DEFAULT_DISPONIBILIDAD + "," + UPDATED_DISPONIBILIDAD,
            "disponibilidad.in=" + UPDATED_DISPONIBILIDAD
        );
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDisponibilidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where disponibilidad is not null
        defaultTrabajadorFiltering("disponibilidad.specified=true", "disponibilidad.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTurnoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where turno equals to
        defaultTrabajadorFiltering("turno.equals=" + DEFAULT_TURNO, "turno.equals=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTurnoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where turno in
        defaultTrabajadorFiltering("turno.in=" + DEFAULT_TURNO + "," + UPDATED_TURNO, "turno.in=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTurnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where turno is not null
        defaultTrabajadorFiltering("turno.specified=true", "turno.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEspecialidadIsEqualToSomething() throws Exception {
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            trabajadorRepository.saveAndFlush(trabajador);
            especialidad = EspecialidadResourceIT.createEntity();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        em.persist(especialidad);
        em.flush();
        trabajador.addEspecialidad(especialidad);
        trabajadorRepository.saveAndFlush(trabajador);
        Long especialidadId = especialidad.getId();
        // Get all the trabajadorList where especialidad equals to especialidadId
        defaultTrabajadorShouldBeFound("especialidadId.equals=" + especialidadId);

        // Get all the trabajadorList where especialidad equals to (especialidadId + 1)
        defaultTrabajadorShouldNotBeFound("especialidadId.equals=" + (especialidadId + 1));
    }

    @Test
    @Transactional
    void getAllTrabajadorsByCitaIsEqualToSomething() throws Exception {
        Cita cita;
        if (TestUtil.findAll(em, Cita.class).isEmpty()) {
            trabajadorRepository.saveAndFlush(trabajador);
            cita = CitaResourceIT.createEntity();
        } else {
            cita = TestUtil.findAll(em, Cita.class).get(0);
        }
        em.persist(cita);
        em.flush();
        trabajador.addCita(cita);
        trabajadorRepository.saveAndFlush(trabajador);
        Long citaId = cita.getId();
        // Get all the trabajadorList where cita equals to citaId
        defaultTrabajadorShouldBeFound("citaId.equals=" + citaId);

        // Get all the trabajadorList where cita equals to (citaId + 1)
        defaultTrabajadorShouldNotBeFound("citaId.equals=" + (citaId + 1));
    }

    @Test
    @Transactional
    void getAllTrabajadorsByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            trabajadorRepository.saveAndFlush(trabajador);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        trabajador.addPaciente(paciente);
        trabajadorRepository.saveAndFlush(trabajador);
        Long pacienteId = paciente.getId();
        // Get all the trabajadorList where paciente equals to pacienteId
        defaultTrabajadorShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the trabajadorList where paciente equals to (pacienteId + 1)
        defaultTrabajadorShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllTrabajadorsByDireccionIsEqualToSomething() throws Exception {
        Direccion direccion;
        if (TestUtil.findAll(em, Direccion.class).isEmpty()) {
            trabajadorRepository.saveAndFlush(trabajador);
            direccion = DireccionResourceIT.createEntity();
        } else {
            direccion = TestUtil.findAll(em, Direccion.class).get(0);
        }
        em.persist(direccion);
        em.flush();
        trabajador.addDireccion(direccion);
        trabajadorRepository.saveAndFlush(trabajador);
        Long direccionId = direccion.getId();
        // Get all the trabajadorList where direccion equals to direccionId
        defaultTrabajadorShouldBeFound("direccionId.equals=" + direccionId);

        // Get all the trabajadorList where direccion equals to (direccionId + 1)
        defaultTrabajadorShouldNotBeFound("direccionId.equals=" + (direccionId + 1));
    }

    private void defaultTrabajadorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrabajadorShouldBeFound(shouldBeFound);
        defaultTrabajadorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrabajadorShouldBeFound(String filter) throws Exception {
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUsuario").value(hasItem(DEFAULT_ID_USUARIO.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].puesto").value(hasItem(DEFAULT_PUESTO.toString())))
            .andExpect(jsonPath("$.[*].disponibilidad").value(hasItem(DEFAULT_DISPONIBILIDAD)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO.toString())));

        // Check, that the count call also returns 1
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrabajadorShouldNotBeFound(String filter) throws Exception {
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrabajador() throws Exception {
        // Get the trabajador
        restTrabajadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador
        Trabajador updatedTrabajador = trabajadorRepository.findById(trabajador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrabajador are not directly saved in db
        em.detach(updatedTrabajador);
        updatedTrabajador
            .idUsuario(UPDATED_ID_USUARIO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dni(UPDATED_DNI)
            .puesto(UPDATED_PUESTO)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .turno(UPDATED_TURNO);
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(updatedTrabajador);

        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrabajadorToMatchAllProperties(updatedTrabajador);
    }

    @Test
    @Transactional
    void putNonExistingTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrabajadorWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador using partial update
        Trabajador partialUpdatedTrabajador = new Trabajador();
        partialUpdatedTrabajador.setId(trabajador.getId());

        partialUpdatedTrabajador.idUsuario(UPDATED_ID_USUARIO).dni(UPDATED_DNI);

        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajador))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajadorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrabajador, trabajador),
            getPersistedTrabajador(trabajador)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrabajadorWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador using partial update
        Trabajador partialUpdatedTrabajador = new Trabajador();
        partialUpdatedTrabajador.setId(trabajador.getId());

        partialUpdatedTrabajador
            .idUsuario(UPDATED_ID_USUARIO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dni(UPDATED_DNI)
            .puesto(UPDATED_PUESTO)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .turno(UPDATED_TURNO);

        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajador))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajadorUpdatableFieldsEquals(partialUpdatedTrabajador, getPersistedTrabajador(partialUpdatedTrabajador));
    }

    @Test
    @Transactional
    void patchNonExistingTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trabajador
        restTrabajadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, trabajador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trabajadorRepository.count();
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

    protected Trabajador getPersistedTrabajador(Trabajador trabajador) {
        return trabajadorRepository.findById(trabajador.getId()).orElseThrow();
    }

    protected void assertPersistedTrabajadorToMatchAllProperties(Trabajador expectedTrabajador) {
        assertTrabajadorAllPropertiesEquals(expectedTrabajador, getPersistedTrabajador(expectedTrabajador));
    }

    protected void assertPersistedTrabajadorToMatchUpdatableProperties(Trabajador expectedTrabajador) {
        assertTrabajadorAllUpdatablePropertiesEquals(expectedTrabajador, getPersistedTrabajador(expectedTrabajador));
    }
}
