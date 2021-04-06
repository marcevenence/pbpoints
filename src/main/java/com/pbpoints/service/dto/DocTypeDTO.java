package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.DocType} entity.
 */
@ApiModel(description = "Address entity.\n@author The JHipster team.")
public class DocTypeDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocTypeDTO docTypeDTO = (DocTypeDTO) o;
        if (docTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), docTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocTypeDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='" + getDescription() + "'" + "}";
    }
}
