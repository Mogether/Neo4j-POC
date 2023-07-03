package com.neo4jpoc.bootifulmusuc.service.mapper;

import com.neo4jpoc.bootifulmusuc.domain.Genre;
import com.neo4jpoc.bootifulmusuc.service.dto.GenreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {}
