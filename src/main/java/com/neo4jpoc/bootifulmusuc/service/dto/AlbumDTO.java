package com.neo4jpoc.bootifulmusuc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.neo4jpoc.bootifulmusuc.domain.Album} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlbumDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private ArtistDTO artist;

    private GenreDTO genre;

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

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlbumDTO)) {
            return false;
        }

        AlbumDTO albumDTO = (AlbumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, albumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlbumDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", artist=" + getArtist() +
            ", genre=" + getGenre() +
            "}";
    }
}
