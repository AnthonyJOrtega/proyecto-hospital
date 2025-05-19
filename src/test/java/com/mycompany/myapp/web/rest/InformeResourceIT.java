package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.InformeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.InformeRepository;
import com.mycompany.myapp.service.InformeService;
import com.mycompany.myapp.service.dto.InformeDTO;
import com.mycompany.myapp.service.mapper.InformeMapper;
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
 * Integration tests for the {@link InformeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InformeResourceIT {

    private static final String DEFAULT_FECHA = "AAAAAAAAAA";
    private static final String UPDATED_FECHA = "BBBBBBBBBB";

    private static final String DEFAULT_RESUMEN = "AAAAAAAAAA";
    private static final String UPDATED_RESUMEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/informes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InformeRepository informeRepository;

    @Mock
    private InformeRepository informeRepositoryMock;

    @Autowired
    private InformeMapper informeMapper;

    @Mock
    private InformeService informeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformeMockMvc;

    private Informe informe;

    private Informe insertedInforme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informe createEntity() {
        return new Informe().fecha(DEFAULT_FECHA).resumen(DEFAULT_RESUMEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informe createUpdatedEntity() {
        return new Informe().fecha(UPDATED_FECHA).resumen(UPDATED_RESUMEN);
    }

    @BeforeEach
    void initTest() {
        informe = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInforme != null) {
            informeRepository.delete(insertedInforme);
            insertedInforme = null;
        }
    }

    @Test
    @Transactional
    void createInforme() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);
        var returnedInformeDTO = om.readValue(
            restInformeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InformeDTO.class
        );

        // Validate the Informe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInforme = informeMapper.toEntity(returnedInformeDTO);
        assertInformeUpdatableFieldsEquals(returnedInforme, getPersistedInforme(returnedInforme));

        insertedInforme = returnedInforme;
    }

    @Test
    @Transactional
    void createInformeWithExistingId() throws Exception {
        // Create the Informe with an existing ID
        informe.setId(1L);
        InformeDTO informeDTO = informeMapper.toDto(informe);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInformes() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList
        restInformeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informe.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA)))
            .andExpect(jsonPath("$.[*].resumen").value(hasItem(DEFAULT_RESUMEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInformesWithEagerRelationshipsIsEnabled() throws Exception {
        when(informeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInformeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(informeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInformesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(informeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInformeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(informeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInforme() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get the informe
        restInformeMockMvc
            .perform(get(ENTITY_API_URL_ID, informe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informe.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA))
            .andExpect(jsonPath("$.resumen").value(DEFAULT_RESUMEN));
    }

    @Test
    @Transactional
    void getInformesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        Long id = informe.getId();

        defaultInformeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInformeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInformeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInformesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where fecha equals to
        defaultInformeFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllInformesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where fecha in
        defaultInformeFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllInformesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where fecha is not null
        defaultInformeFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllInformesByFechaContainsSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where fecha contains
        defaultInformeFiltering("fecha.contains=" + DEFAULT_FECHA, "fecha.contains=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllInformesByFechaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where fecha does not contain
        defaultInformeFiltering("fecha.doesNotContain=" + UPDATED_FECHA, "fecha.doesNotContain=" + DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void getAllInformesByResumenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where resumen equals to
        defaultInformeFiltering("resumen.equals=" + DEFAULT_RESUMEN, "resumen.equals=" + UPDATED_RESUMEN);
    }

    @Test
    @Transactional
    void getAllInformesByResumenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where resumen in
        defaultInformeFiltering("resumen.in=" + DEFAULT_RESUMEN + "," + UPDATED_RESUMEN, "resumen.in=" + UPDATED_RESUMEN);
    }

    @Test
    @Transactional
    void getAllInformesByResumenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where resumen is not null
        defaultInformeFiltering("resumen.specified=true", "resumen.specified=false");
    }

    @Test
    @Transactional
    void getAllInformesByResumenContainsSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where resumen contains
        defaultInformeFiltering("resumen.contains=" + DEFAULT_RESUMEN, "resumen.contains=" + UPDATED_RESUMEN);
    }

    @Test
    @Transactional
    void getAllInformesByResumenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        // Get all the informeList where resumen does not contain
        defaultInformeFiltering("resumen.doesNotContain=" + UPDATED_RESUMEN, "resumen.doesNotContain=" + DEFAULT_RESUMEN);
    }

    @Test
    @Transactional
    void getAllInformesByRecetaIsEqualToSomething() throws Exception {
        Receta receta;
        if (TestUtil.findAll(em, Receta.class).isEmpty()) {
            informeRepository.saveAndFlush(informe);
            receta = RecetaResourceIT.createEntity();
        } else {
            receta = TestUtil.findAll(em, Receta.class).get(0);
        }
        em.persist(receta);
        em.flush();
        informe.setReceta(receta);
        informeRepository.saveAndFlush(informe);
        Long recetaId = receta.getId();
        // Get all the informeList where receta equals to recetaId
        defaultInformeShouldBeFound("recetaId.equals=" + recetaId);

        // Get all the informeList where receta equals to (recetaId + 1)
        defaultInformeShouldNotBeFound("recetaId.equals=" + (recetaId + 1));
    }

    @Test
    @Transactional
    void getAllInformesByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            informeRepository.saveAndFlush(informe);
            paciente = PacienteResourceIT.createEntity();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        informe.setPaciente(paciente);
        informeRepository.saveAndFlush(informe);
        Long pacienteId = paciente.getId();
        // Get all the informeList where paciente equals to pacienteId
        defaultInformeShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the informeList where paciente equals to (pacienteId + 1)
        defaultInformeShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllInformesByTrabajadorIsEqualToSomething() throws Exception {
        Trabajador trabajador;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            informeRepository.saveAndFlush(informe);
            trabajador = TrabajadorResourceIT.createEntity();
        } else {
            trabajador = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajador);
        em.flush();
        informe.setTrabajador(trabajador);
        informeRepository.saveAndFlush(informe);
        Long trabajadorId = trabajador.getId();
        // Get all the informeList where trabajador equals to trabajadorId
        defaultInformeShouldBeFound("trabajadorId.equals=" + trabajadorId);

        // Get all the informeList where trabajador equals to (trabajadorId + 1)
        defaultInformeShouldNotBeFound("trabajadorId.equals=" + (trabajadorId + 1));
    }

    @Test
    @Transactional
    void getAllInformesByEnfermedadIsEqualToSomething() throws Exception {
        Enfermedad enfermedad;
        if (TestUtil.findAll(em, Enfermedad.class).isEmpty()) {
            informeRepository.saveAndFlush(informe);
            enfermedad = EnfermedadResourceIT.createEntity();
        } else {
            enfermedad = TestUtil.findAll(em, Enfermedad.class).get(0);
        }
        em.persist(enfermedad);
        em.flush();
        informe.addEnfermedad(enfermedad);
        informeRepository.saveAndFlush(informe);
        Long enfermedadId = enfermedad.getId();
        // Get all the informeList where enfermedad equals to enfermedadId
        defaultInformeShouldBeFound("enfermedadId.equals=" + enfermedadId);

        // Get all the informeList where enfermedad equals to (enfermedadId + 1)
        defaultInformeShouldNotBeFound("enfermedadId.equals=" + (enfermedadId + 1));
    }

    private void defaultInformeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInformeShouldBeFound(shouldBeFound);
        defaultInformeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInformeShouldBeFound(String filter) throws Exception {
        restInformeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informe.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA)))
            .andExpect(jsonPath("$.[*].resumen").value(hasItem(DEFAULT_RESUMEN)));

        // Check, that the count call also returns 1
        restInformeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInformeShouldNotBeFound(String filter) throws Exception {
        restInformeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInformeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInforme() throws Exception {
        // Get the informe
        restInformeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInforme() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the informe
        Informe updatedInforme = informeRepository.findById(informe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInforme are not directly saved in db
        em.detach(updatedInforme);
        updatedInforme.fecha(UPDATED_FECHA).resumen(UPDATED_RESUMEN);
        InformeDTO informeDTO = informeMapper.toDto(updatedInforme);

        restInformeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInformeToMatchAllProperties(updatedInforme);
    }

    @Test
    @Transactional
    void putNonExistingInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(informeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInformeWithPatch() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the informe using partial update
        Informe partialUpdatedInforme = new Informe();
        partialUpdatedInforme.setId(informe.getId());

        partialUpdatedInforme.fecha(UPDATED_FECHA);

        restInformeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInforme.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInforme))
            )
            .andExpect(status().isOk());

        // Validate the Informe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedInforme, informe), getPersistedInforme(informe));
    }

    @Test
    @Transactional
    void fullUpdateInformeWithPatch() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the informe using partial update
        Informe partialUpdatedInforme = new Informe();
        partialUpdatedInforme.setId(informe.getId());

        partialUpdatedInforme.fecha(UPDATED_FECHA).resumen(UPDATED_RESUMEN);

        restInformeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInforme.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInforme))
            )
            .andExpect(status().isOk());

        // Validate the Informe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformeUpdatableFieldsEquals(partialUpdatedInforme, getPersistedInforme(partialUpdatedInforme));
    }

    @Test
    @Transactional
    void patchNonExistingInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, informeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(informeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(informeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInforme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        informe.setId(longCount.incrementAndGet());

        // Create the Informe
        InformeDTO informeDTO = informeMapper.toDto(informe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(informeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Informe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInforme() throws Exception {
        // Initialize the database
        insertedInforme = informeRepository.saveAndFlush(informe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the informe
        restInformeMockMvc
            .perform(delete(ENTITY_API_URL_ID, informe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return informeRepository.count();
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

    protected Informe getPersistedInforme(Informe informe) {
        return informeRepository.findById(informe.getId()).orElseThrow();
    }

    protected void assertPersistedInformeToMatchAllProperties(Informe expectedInforme) {
        assertInformeAllPropertiesEquals(expectedInforme, getPersistedInforme(expectedInforme));
    }

    protected void assertPersistedInformeToMatchUpdatableProperties(Informe expectedInforme) {
        assertInformeAllUpdatablePropertiesEquals(expectedInforme, getPersistedInforme(expectedInforme));
    }
}
