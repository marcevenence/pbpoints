package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Equipment} entity.
 */
public class EquipmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @Lob
    private byte[] picture1;

    private String picture1ContentType;

    @Lob
    private byte[] picture2;

    private String picture2ContentType;
    private String serial;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte[] getPicture1() {
        return picture1;
    }

    public void setPicture1(byte[] picture1) {
        this.picture1 = picture1;
    }

    public String getPicture1ContentType() {
        return picture1ContentType;
    }

    public void setPicture1ContentType(String picture1ContentType) {
        this.picture1ContentType = picture1ContentType;
    }

    public byte[] getPicture2() {
        return picture2;
    }

    public void setPicture2(byte[] picture2) {
        this.picture2 = picture2;
    }

    public String getPicture2ContentType() {
        return picture2ContentType;
    }

    public void setPicture2ContentType(String picture2ContentType) {
        this.picture2ContentType = picture2ContentType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentDTO)) {
            return false;
        }

        EquipmentDTO equipmentDTO = (EquipmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentDTO{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", picture1='" + getPicture1() + "'" +
            ", picture2='" + getPicture2() + "'" +
            ", serial='" + getSerial() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
