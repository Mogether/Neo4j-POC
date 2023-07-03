package com.neo4jpoc.bootifulmusuc.service.mapper;

import com.neo4jpoc.bootifulmusuc.domain.Album;
import com.neo4jpoc.bootifulmusuc.domain.Artist;
import com.neo4jpoc.bootifulmusuc.domain.Genre;
import com.neo4jpoc.bootifulmusuc.service.dto.AlbumDTO;
import com.neo4jpoc.bootifulmusuc.service.dto.ArtistDTO;
import com.neo4jpoc.bootifulmusuc.service.dto.GenreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {
    @Mapping(target = "artist", source = "artist", qualifiedByName = "artistName")
    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreName")
    AlbumDTO toDto(Album s);

    @Named("artistName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArtistDTO toDtoArtistName(Artist artist);

    @Named("genreName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GenreDTO toDtoGenreName(Genre genre);
}
