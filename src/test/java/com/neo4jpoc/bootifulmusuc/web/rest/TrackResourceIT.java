package com.neo4jpoc.bootifulmusuc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neo4jpoc.bootifulmusuc.IntegrationTest;
import com.neo4jpoc.bootifulmusuc.domain.Track;
import com.neo4jpoc.bootifulmusuc.repository.TrackRepository;
import com.neo4jpoc.bootifulmusuc.service.dto.TrackDTO;
import com.neo4jpoc.bootifulmusuc.service.mapper.TrackMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link TrackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private MockMvc restTrackMockMvc;

    private Track track;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Track createEntity() {
        Track track = new Track().name(DEFAULT_NAME);
        return track;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Track createUpdatedEntity() {
        Track track = new Track().name(UPDATED_NAME);
        return track;
    }

    @BeforeEach
    public void initTest() {
        trackRepository.deleteAll();
        track = createEntity();
    }

    @Test
    void createTrack() throws Exception {
        int databaseSizeBeforeCreate = trackRepository.findAll().size();
        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);
        restTrackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackDTO)))
            .andExpect(status().isCreated());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeCreate + 1);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createTrackWithExistingId() throws Exception {
        // Create the Track with an existing ID
        track.setId("existing_id");
        TrackDTO trackDTO = trackMapper.toDto(track);

        int databaseSizeBeforeCreate = trackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        // set the field null
        track.setName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackDTO)))
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTracks() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        // Get all the trackList
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getTrack() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        // Get the track
        restTrackMockMvc
            .perform(get(ENTITY_API_URL_ID, track.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingTrack() throws Exception {
        // Get the track
        restTrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTrack() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();

        // Update the track
        Track updatedTrack = trackRepository.findById(track.getId()).get();
        updatedTrack.name(UPDATED_NAME);
        TrackDTO trackDTO = trackMapper.toDto(updatedTrack);

        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTrackWithPatch() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();

        // Update the track using partial update
        Track partialUpdatedTrack = new Track();
        partialUpdatedTrack.setId(track.getId());

        partialUpdatedTrack.name(UPDATED_NAME);

        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrack))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateTrackWithPatch() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();

        // Update the track using partial update
        Track partialUpdatedTrack = new Track();
        partialUpdatedTrack.setId(track.getId());

        partialUpdatedTrack.name(UPDATED_NAME);

        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrack))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        track.setId(UUID.randomUUID().toString());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTrack() throws Exception {
        // Initialize the database
        trackRepository.save(track);

        int databaseSizeBeforeDelete = trackRepository.findAll().size();

        // Delete the track
        restTrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, track.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
