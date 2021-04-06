package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.City} entity.
 */
@ApiModel(description = "City entity.\n@author Marcelo Miño\nDatos traidos desde https:")
public class CityDTO implements Serializable {

    private Long id;

    private String name;

    private String latitude;

    private String longitude;

    private Long provinceId;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (cityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "CityDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", latitude='" +
            getLatitude() +
            "'" +
            ", longitude='" +
            getLongitude() +
            "'" +
            ", province=" +
            getProvinceId() +
            "}"
        );
    }
}
