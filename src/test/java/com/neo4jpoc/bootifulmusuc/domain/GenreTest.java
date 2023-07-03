package com.neo4jpoc.bootifulmusuc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.neo4jpoc.bootifulmusuc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genre.class);
        Genre genre1 = new Genre();
        genre1.setId("id1");
        Genre genre2 = new Genre();
        genre2.setId(genre1.getId());
        assertThat(genre1).isEqualTo(genre2);
        genre2.setId("id2");
        assertThat(genre1).isNotEqualTo(genre2);
        genre1.setId(null);
        assertThat(genre1).isNotEqualTo(genre2);
    }
}
