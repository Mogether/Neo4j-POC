package com.neo4jpoc.bootifulmusuc.service.mapper;

import com.neo4jpoc.bootifulmusuc.domain.Album;
import com.neo4jpoc.bootifulmusuc.domain.Track;
import com.neo4jpoc.bootifulmusuc.service.dto.AlbumDTO;
import com.neo4jpoc.bootifulmusuc.service.dto.TrackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Track} and its DTO {@link TrackDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrackMapper extends EntityMapper<TrackDTO, Track> {
    @Mapping(target = "album", source = "album", qualifiedByName = "albumName")
    TrackDTO toDto(Track s);

    @Named("albumName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AlbumDTO toDtoAlbumName(Album album);
}
