package com.neo4jpoc.bootifulmusuc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.neo4jpoc.bootifulmusuc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlbumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Album.class);
        Album album1 = new Album();
        album1.setId("id1");
        Album album2 = new Album();
        album2.setId(album1.getId());
        assertThat(album1).isEqualTo(album2);
        album2.setId("id2");
        assertThat(album1).isNotEqualTo(album2);
        album1.setId(null);
        assertThat(album1).isNotEqualTo(album2);
    }
}
