package com.neo4jpoc.bootifulmusuc.service.mapper;

import com.neo4jpoc.bootifulmusuc.domain.Artist;
import com.neo4jpoc.bootifulmusuc.service.dto.ArtistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Artist} and its DTO {@link ArtistDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArtistMapper extends EntityMapper<ArtistDTO, Artist> {}
