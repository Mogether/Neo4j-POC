package com.neo4jpoc.bootifulmusuc.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackMapperTest {

    private TrackMapper trackMapper;

    @BeforeEach
    public void setUp() {
        trackMapper = new TrackMapperImpl();
    }
}
