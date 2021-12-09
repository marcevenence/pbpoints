package com.pbpoints.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @Lob
    @Column(name = "picture_1", nullable = false)
    private byte[] picture1;

    @Column(name = "picture_1_content_type", nullable = false)
    private String picture1ContentType;

    @Lob
    @Column(name = "picture_2", nullable = false)
    private byte[] picture2;

    @Column(name = "picture_2_content_type", nullable = false)
    private String picture2ContentType;

    @Column(name = "serial")
    private String serial;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipment id(Long id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return this.brand;
    }

    public Equipment brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Equipment model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte[] getPicture1() {
        return this.picture1;
    }

    public Equipment picture1(byte[] picture1) {
        this.picture1 = picture1;
        return this;
    }

    public void setPicture1(byte[] picture1) {
        this.picture1 = picture1;
    }

    public String getPicture1ContentType() {
        return this.picture1ContentType;
    }

    public Equipment picture1ContentType(String picture1ContentType) {
        this.picture1ContentType = picture1ContentType;
        return this;
    }

    public void setPicture1ContentType(String picture1ContentType) {
        this.picture1ContentType = picture1ContentType;
    }

    public byte[] getPicture2() {
        return this.picture2;
    }

    public Equipment picture2(byte[] picture2) {
        this.picture2 = picture2;
        return this;
    }

    public void setPicture2(byte[] picture2) {
        this.picture2 = picture2;
    }

    public String getPicture2ContentType() {
        return this.picture2ContentType;
    }

    public Equipment picture2ContentType(String picture2ContentType) {
        this.picture2ContentType = picture2ContentType;
        return this;
    }

    public void setPicture2ContentType(String picture2ContentType) {
        this.picture2ContentType = picture2ContentType;
    }

    public String getSerial() {
        return this.serial;
    }

    public Equipment serial(String serial) {
        this.serial = serial;
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public User getUser() {
        return this.user;
    }

    public Equipment user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipment)) {
            return false;
        }
        return id != null && id.equals(((Equipment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", picture1=''" +
            ", picture1ContentType='" + getPicture1ContentType() + "'" +
            ", picture2=''" +
            ", picture2ContentType='" + getPicture2ContentType() + "'" +
            ", serial='" + getSerial() + "'" +
            "}";
    }
}
