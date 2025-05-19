package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EnfermedadAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.repository.EnfermedadRepository;
import com.mycompany.myapp.service.dto.EnfermedadDTO;
import com.mycompany.myapp.service.mapper.EnfermedadMapper;
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
 * Integration tests for the {@link EnfermedadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnfermedadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enfermedads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Autowired
    private EnfermedadMapper enfermedadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnfermedadMockMvc;

    private Enfermedad enfermedad;

    private Enfermedad insertedEnfermedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createEntity() {
        return new Enfermedad().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createUpdatedEntity() {
        return new Enfermedad().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        enfermedad = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEnfermedad != null) {
            enfermedadRepository.delete(insertedEnfermedad);
            insertedEnfermedad = null;
        }
    }

    @Test
    @Transactional
    void createEnfermedad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);
        var returnedEnfermedadDTO = om.readValue(
            restEnfermedadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnfermedadDTO.class
        );

        // Validate the Enfermedad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnfermedad = enfermedadMapper.toEntity(returnedEnfermedadDTO);
        assertEnfermedadUpdatableFieldsEquals(returnedEnfermedad, getPersistedEnfermedad(returnedEnfermedad));

        insertedEnfermedad = returnedEnfermedad;
    }

    @Test
    @Transactional
    void createEnfermedadWithExistingId() throws Exception {
        // Create the Enfermedad with an existing ID
        enfermedad.setId(1L);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfermedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnfermedads() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getEnfermedad() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get the enfermedad
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL_ID, enfermedad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enfermedad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getEnfermedadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        Long id = enfermedad.getId();

        defaultEnfermedadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEnfermedadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEnfermedadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where nombre equals to
        defaultEnfermedadFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where nombre in
        defaultEnfermedadFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where nombre is not null
        defaultEnfermedadFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermedadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where nombre contains
        defaultEnfermedadFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where nombre does not contain
        defaultEnfermedadFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where descripcion equals to
        defaultEnfermedadFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where descripcion in
        defaultEnfermedadFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEnfermedadsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where descripcion is not null
        defaultEnfermedadFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermedadsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where descripcion contains
        defaultEnfermedadFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEnfermedadsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList where descripcion does not contain
        defaultEnfermedadFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEnfermedadsByInformeIsEqualToSomething() throws Exception {
        Informe informe;
        if (TestUtil.findAll(em, Informe.class).isEmpty()) {
            enfermedadRepository.saveAndFlush(enfermedad);
            informe = InformeResourceIT.createEntity();
        } else {
            informe = TestUtil.findAll(em, Informe.class).get(0);
        }
        em.persist(informe);
        em.flush();
        enfermedad.addInforme(informe);
        enfermedadRepository.saveAndFlush(enfermedad);
        Long informeId = informe.getId();
        // Get all the enfermedadList where informe equals to informeId
        defaultEnfermedadShouldBeFound("informeId.equals=" + informeId);

        // Get all the enfermedadList where informe equals to (informeId + 1)
        defaultEnfermedadShouldNotBeFound("informeId.equals=" + (informeId + 1));
    }

    private void defaultEnfermedadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEnfermedadShouldBeFound(shouldBeFound);
        defaultEnfermedadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnfermedadShouldBeFound(String filter) throws Exception {
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnfermedadShouldNotBeFound(String filter) throws Exception {
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnfermedad() throws Exception {
        // Get the enfermedad
        restEnfermedadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnfermedad() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad
        Enfermedad updatedEnfermedad = enfermedadRepository.findById(enfermedad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnfermedad are not directly saved in db
        em.detach(updatedEnfermedad);
        updatedEnfermedad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(updatedEnfermedad);

        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnfermedadToMatchAllProperties(updatedEnfermedad);
    }

    @Test
    @Transactional
    void putNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermedad))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnfermedad, enfermedad),
            getPersistedEnfermedad(enfermedad)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermedad))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(partialUpdatedEnfermedad, getPersistedEnfermedad(partialUpdatedEnfermedad));
    }

    @Test
    @Transactional
    void patchNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnfermedad() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enfermedad
        restEnfermedadMockMvc
            .perform(delete(ENTITY_API_URL_ID, enfermedad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enfermedadRepository.count();
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

    protected Enfermedad getPersistedEnfermedad(Enfermedad enfermedad) {
        return enfermedadRepository.findById(enfermedad.getId()).orElseThrow();
    }

    protected void assertPersistedEnfermedadToMatchAllProperties(Enfermedad expectedEnfermedad) {
        assertEnfermedadAllPropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }

    protected void assertPersistedEnfermedadToMatchUpdatableProperties(Enfermedad expectedEnfermedad) {
        assertEnfermedadAllUpdatablePropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }
}
