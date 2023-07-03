package com.neo4jpoc.bootifulmusuc.repository;

import com.neo4jpoc.bootifulmusuc.domain.Artist;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Artist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtistRepository extends Neo4jRepository<Artist, String> {}
