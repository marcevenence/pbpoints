package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.Province} entity.
 */
@ApiModel(description = "Province entity.\n@author Marcelo Miño\nDatos traidos desde https:")
public class ProvinceDTO implements Serializable {

    private Long id;

    private String name;

    private CountryDTO country;

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

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProvinceDTO)) {
            return false;
        }

        ProvinceDTO provinceDTO = (ProvinceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, provinceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
