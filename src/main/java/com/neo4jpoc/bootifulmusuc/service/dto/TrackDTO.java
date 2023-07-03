package com.neo4jpoc.bootifulmusuc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.neo4jpoc.bootifulmusuc.domain.Track} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private AlbumDTO album;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackDTO)) {
            return false;
        }

        TrackDTO trackDTO = (TrackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", album=" + getAlbum() +
            "}";
    }
}
