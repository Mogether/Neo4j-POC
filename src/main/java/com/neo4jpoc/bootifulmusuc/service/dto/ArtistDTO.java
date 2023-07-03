package com.neo4jpoc.bootifulmusuc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.neo4jpoc.bootifulmusuc.domain.Artist} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArtistDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtistDTO)) {
            return false;
        }

        ArtistDTO artistDTO = (ArtistDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, artistDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
