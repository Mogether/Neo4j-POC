package com.neo4jpoc.bootifulmusuc.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArtistMapperTest {

    private ArtistMapper artistMapper;

    @BeforeEach
    public void setUp() {
        artistMapper = new ArtistMapperImpl();
    }
}
