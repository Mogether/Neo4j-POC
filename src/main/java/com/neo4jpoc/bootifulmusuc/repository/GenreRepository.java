package com.neo4jpoc.bootifulmusuc.repository;

import com.neo4jpoc.bootifulmusuc.domain.Genre;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Genre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenreRepository extends Neo4jRepository<Genre, String> {}
