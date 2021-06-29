package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.UserExtra} entity.
 */
@ApiModel(description = "UserExtra entity.\n@author Marcelo Miño")
public class UserExtraDTO implements Serializable {

    private Long id;

    private String numDoc;

    private String phone;

    private LocalDate bornDate;

    @Lob
    private byte[] picture;

    private String pictureContentType;

    private DocTypeDTO docType;

    @NotNull
    private String code;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public DocTypeDTO getDocType() {
        return docType;
    }

    public void setDocType(DocTypeDTO docType) {
        this.docType = docType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(o instanceof UserExtraDTO)) {
            return false;
        }

        UserExtraDTO userExtraDTO = (UserExtraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userExtraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraDTO{" +
            "id=" + getId() +
            ", numDoc='" + getNumDoc() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bornDate='" + getBornDate() + "'" +
            ", picture='" + getPicture() + "'" +
            ", code='" + getCode() + "'" +
            ", user=" + getUser() +
            ", docType=" + getDocType() +
            "}";
    }
}
