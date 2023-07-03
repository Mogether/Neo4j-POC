package com.neo4jpoc.bootifulmusuc.repository;

import com.neo4jpoc.bootifulmusuc.domain.Album;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Album entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends Neo4jRepository<Album, String> {}
