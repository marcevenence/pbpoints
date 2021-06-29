package com.pbpoints.web.rest.vm;

import com.pbpoints.domain.DocType;
import com.pbpoints.service.dto.UserDTO;
import java.time.LocalDate;
import javax.persistence.Lob;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    //Agregado Edu 20191023 UserExtra
    private String phone;

    private String numDoc;

    private LocalDate bornDate;

    private String password;

    private byte[] picture;

    private String pictureContentType;

    private String code;

    private DocType docType;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    @Override
    public String toString() {
        return (
            "ManagedUserVM [" +
            super.toString() +
            ", phone=" +
            phone +
            ", numDoc=" +
            numDoc +
            ", bornDate=" +
            bornDate +
            ", code=" +
            code +
            ", docType=" +
            docType +
            "]"
        );
    }
}
