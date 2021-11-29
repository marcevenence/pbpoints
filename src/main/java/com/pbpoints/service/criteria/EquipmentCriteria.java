package com.pbpoints.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pbpoints.domain.Equipment} entity. This class is used
 * in {@link com.pbpoints.web.rest.EquipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter brand;

    private StringFilter model;

    private StringFilter serial;

    private LongFilter userId;

    public EquipmentCriteria() {}

    public EquipmentCriteria(EquipmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.serial = other.serial == null ? null : other.serial.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public EquipmentCriteria copy() {
        return new EquipmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public StringFilter brand() {
        if (brand == null) {
            brand = new StringFilter();
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getSerial() {
        return serial;
    }

    public StringFilter serial() {
        if (serial == null) {
            serial = new StringFilter();
        }
        return serial;
    }

    public void setSerial(StringFilter serial) {
        this.serial = serial;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EquipmentCriteria that = (EquipmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, serial, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (brand != null ? "brand=" + brand + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (serial != null ? "serial=" + serial + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
