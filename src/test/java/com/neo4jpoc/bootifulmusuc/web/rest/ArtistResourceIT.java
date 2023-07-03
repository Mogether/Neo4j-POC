package com.neo4jpoc.bootifulmusuc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neo4jpoc.bootifulmusuc.IntegrationTest;
import com.neo4jpoc.bootifulmusuc.domain.Artist;
import com.neo4jpoc.bootifulmusuc.repository.ArtistRepository;
import com.neo4jpoc.bootifulmusuc.service.dto.ArtistDTO;
import com.neo4jpoc.bootifulmusuc.service.mapper.ArtistMapper;
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
 * Integration tests for the {@link ArtistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArtistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/artists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private MockMvc restArtistMockMvc;

    private Artist artist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createEntity() {
        Artist artist = new Artist().name(DEFAULT_NAME);
        return artist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createUpdatedEntity() {
        Artist artist = new Artist().name(UPDATED_NAME);
        return artist;
    }

    @BeforeEach
    public void initTest() {
        artistRepository.deleteAll();
        artist = createEntity();
    }

    @Test
    void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();
        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);
        restArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createArtistWithExistingId() throws Exception {
        // Create the Artist with an existing ID
        artist.setId("existing_id");
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setName(null);

        // Create the Artist, which fails.
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        restArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isBadRequest());

        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        // Get all the artistList
        restArtistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getArtist() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        // Get the artist
        restArtistMockMvc
            .perform(get(ENTITY_API_URL_ID, artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingArtist() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        Artist updatedArtist = artistRepository.findById(artist.getId()).get();
        updatedArtist.name(UPDATED_NAME);
        ArtistDTO artistDTO = artistMapper.toDto(updatedArtist);

        restArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, artistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, artistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArtistWithPatch() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist using partial update
        Artist partialUpdatedArtist = new Artist();
        partialUpdatedArtist.setId(artist.getId());

        restArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtist))
            )
            .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateArtistWithPatch() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist using partial update
        Artist partialUpdatedArtist = new Artist();
        partialUpdatedArtist.setId(artist.getId());

        partialUpdatedArtist.name(UPDATED_NAME);

        restArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtist))
            )
            .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, artistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(UUID.randomUUID().toString());

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(artistDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArtist() throws Exception {
        // Initialize the database
        artistRepository.save(artist);

        int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Delete the artist
        restArtistMockMvc
            .perform(delete(ENTITY_API_URL_ID, artist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
