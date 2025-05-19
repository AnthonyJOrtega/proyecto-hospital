package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EspecialidadAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Especialidad;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.EspecialidadRepository;
import com.mycompany.myapp.service.dto.EspecialidadDTO;
import com.mycompany.myapp.service.mapper.EspecialidadMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EspecialidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspecialidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especialidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private EspecialidadMapper especialidadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspecialidadMockMvc;

    private Especialidad especialidad;

    private Especialidad insertedEspecialidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidad createEntity() {
        return new Especialidad().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidad createUpdatedEntity() {
        return new Especialidad().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        especialidad = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEspecialidad != null) {
            especialidadRepository.delete(insertedEspecialidad);
            insertedEspecialidad = null;
        }
    }

    @Test
    @Transactional
    void createEspecialidad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);
        var returnedEspecialidadDTO = om.readValue(
            restEspecialidadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialidadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EspecialidadDTO.class
        );

        // Validate the Especialidad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEspecialidad = especialidadMapper.toEntity(returnedEspecialidadDTO);
        assertEspecialidadUpdatableFieldsEquals(returnedEspecialidad, getPersistedEspecialidad(returnedEspecialidad));

        insertedEspecialidad = returnedEspecialidad;
    }

    @Test
    @Transactional
    void createEspecialidadWithExistingId() throws Exception {
        // Create the Especialidad with an existing ID
        especialidad.setId(1L);
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialidadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEspecialidads() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getEspecialidad() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get the especialidad
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL_ID, especialidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especialidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getEspecialidadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        Long id = especialidad.getId();

        defaultEspecialidadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEspecialidadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEspecialidadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where nombre equals to
        defaultEspecialidadFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where nombre in
        defaultEspecialidadFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where nombre is not null
        defaultEspecialidadFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialidadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where nombre contains
        defaultEspecialidadFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where nombre does not contain
        defaultEspecialidadFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where descripcion equals to
        defaultEspecialidadFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where descripcion in
        defaultEspecialidadFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEspecialidadsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where descripcion is not null
        defaultEspecialidadFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialidadsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where descripcion contains
        defaultEspecialidadFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEspecialidadsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        // Get all the especialidadList where descripcion does not contain
        defaultEspecialidadFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEspecialidadsByTrabajadorIsEqualToSomething() throws Exception {
        Trabajador trabajador;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            especialidadRepository.saveAndFlush(especialidad);
            trabajador = TrabajadorResourceIT.createEntity();
        } else {
            trabajador = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajador);
        em.flush();
        especialidad.addTrabajador(trabajador);
        especialidadRepository.saveAndFlush(especialidad);
        Long trabajadorId = trabajador.getId();
        // Get all the especialidadList where trabajador equals to trabajadorId
        defaultEspecialidadShouldBeFound("trabajadorId.equals=" + trabajadorId);

        // Get all the especialidadList where trabajador equals to (trabajadorId + 1)
        defaultEspecialidadShouldNotBeFound("trabajadorId.equals=" + (trabajadorId + 1));
    }

    private void defaultEspecialidadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEspecialidadShouldBeFound(shouldBeFound);
        defaultEspecialidadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEspecialidadShouldBeFound(String filter) throws Exception {
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEspecialidadShouldNotBeFound(String filter) throws Exception {
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEspecialidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEspecialidad() throws Exception {
        // Get the especialidad
        restEspecialidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEspecialidad() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialidad
        Especialidad updatedEspecialidad = especialidadRepository.findById(especialidad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEspecialidad are not directly saved in db
        em.detach(updatedEspecialidad);
        updatedEspecialidad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(updatedEspecialidad);

        restEspecialidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especialidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especialidadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEspecialidadToMatchAllProperties(updatedEspecialidad);
    }

    @Test
    @Transactional
    void putNonExistingEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especialidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especialidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especialidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspecialidadWithPatch() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialidad using partial update
        Especialidad partialUpdatedEspecialidad = new Especialidad();
        partialUpdatedEspecialidad.setId(especialidad.getId());

        partialUpdatedEspecialidad.nombre(UPDATED_NOMBRE);

        restEspecialidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecialidad))
            )
            .andExpect(status().isOk());

        // Validate the Especialidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecialidadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEspecialidad, especialidad),
            getPersistedEspecialidad(especialidad)
        );
    }

    @Test
    @Transactional
    void fullUpdateEspecialidadWithPatch() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialidad using partial update
        Especialidad partialUpdatedEspecialidad = new Especialidad();
        partialUpdatedEspecialidad.setId(especialidad.getId());

        partialUpdatedEspecialidad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restEspecialidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecialidad))
            )
            .andExpect(status().isOk());

        // Validate the Especialidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecialidadUpdatableFieldsEquals(partialUpdatedEspecialidad, getPersistedEspecialidad(partialUpdatedEspecialidad));
    }

    @Test
    @Transactional
    void patchNonExistingEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especialidadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especialidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especialidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspecialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialidad.setId(longCount.incrementAndGet());

        // Create the Especialidad
        EspecialidadDTO especialidadDTO = especialidadMapper.toDto(especialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialidadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(especialidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspecialidad() throws Exception {
        // Initialize the database
        insertedEspecialidad = especialidadRepository.saveAndFlush(especialidad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the especialidad
        restEspecialidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, especialidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return especialidadRepository.count();
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

    protected Especialidad getPersistedEspecialidad(Especialidad especialidad) {
        return especialidadRepository.findById(especialidad.getId()).orElseThrow();
    }

    protected void assertPersistedEspecialidadToMatchAllProperties(Especialidad expectedEspecialidad) {
        assertEspecialidadAllPropertiesEquals(expectedEspecialidad, getPersistedEspecialidad(expectedEspecialidad));
    }

    protected void assertPersistedEspecialidadToMatchUpdatableProperties(Especialidad expectedEspecialidad) {
        assertEspecialidadAllUpdatablePropertiesEquals(expectedEspecialidad, getPersistedEspecialidad(expectedEspecialidad));
    }
}
