package com.neo4jpoc.bootifulmusuc.repository;

import com.neo4jpoc.bootifulmusuc.domain.Track;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Track entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackRepository extends Neo4jRepository<Track, String> {}
