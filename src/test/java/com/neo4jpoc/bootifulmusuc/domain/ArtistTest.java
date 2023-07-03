package com.neo4jpoc.bootifulmusuc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.neo4jpoc.bootifulmusuc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArtistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artist.class);
        Artist artist1 = new Artist();
        artist1.setId("id1");
        Artist artist2 = new Artist();
        artist2.setId(artist1.getId());
        assertThat(artist1).isEqualTo(artist2);
        artist2.setId("id2");
        assertThat(artist1).isNotEqualTo(artist2);
        artist1.setId(null);
        assertThat(artist1).isNotEqualTo(artist2);
    }
}
